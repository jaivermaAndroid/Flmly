package com.flmly.tv.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flmly.tv.R
import com.flmly.tv.utility.Api
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.json.JSONException
import org.json.JSONObject


class PlayerActivity : Activity() {
    private val TAG = PlayerActivity::class.java.simpleName
    var playerView: PlayerView? = null
    private var exoPlayer: SimpleExoPlayer? = null
    private var mIsLongPress = false
    lateinit var progressBar: ProgressBar
    private var playbackPosition: Long = 0
    lateinit var tvdetailssignupPrice: AppCompatTextView
    private var videoUrl:String?=null
    lateinit var tvSignupText:AppCompatTextView
    lateinit var tvTitle:AppCompatTextView
    lateinit var tvHeader:AppCompatTextView
    lateinit var subscribe_details_Layout: ConstraintLayout
    lateinit var layout_signup_details: ConstraintLayout
    var duration:Int=0
    var currentTime:Long=0
    var subMessage:String=""
    lateinit var tvdetails_sub_Back: TextView



    val handlerTask: Runnable = object : Runnable {
        @RequiresApi(api = Build.VERSION_CODES.N)
        override fun run() {


            if(exoPlayer!=null) {
                currentTime = exoPlayer!!.getCurrentPosition() / 1000
                Log.d("zzz", "current Time  " + currentTime + "duration " + duration)
                if (currentTime > duration ) {
//            if (currentTime >=10) {
                    Log.d("zzz", "inside handler condition  " + currentTime + "  duration " + 10 * 1000.toLong())
                    stopTask()
                    pausePlayer()
                    releasePlayer()
                    if (subMessage == "SUBSCRIPTION_EXPIRED") {
                        subscribe_details_Layout.visibility = View.VISIBLE
                        playerView!!.visibility=View.GONE

                    } else {
                        layout_signup_details.visibility = View.VISIBLE
                        tvdetails_sub_Back.requestFocus()
                        playerView!!.visibility=View.GONE

                    }
                } else {
                    Log.d("zzz", "inside handler else  " + currentTime)
                    Handler().postDelayed(this, 1000)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        progressBar=findViewById(R.id.playerProgress)
        playerView = findViewById(R.id.playerView)
        tvTitle=findViewById(R.id.tvExoTitle)
        tvHeader=findViewById(R.id.tvExoHeader)
        tvdetailssignupPrice=findViewById(R.id.tvdetailssignupPrice)
        videoUrl = intent.getStringExtra("promoUrl")
        subMessage = intent.getStringExtra("sub").toString()

        Log.d("player","Url"+subMessage)

        subscribe_details_Layout = findViewById(R.id.subscribe_details_Layout)
        layout_signup_details = findViewById(R.id.layout_signup_details)
        tvdetails_sub_Back = findViewById(R.id.tvdetails_sub_Back)
        tvSignupText=findViewById(R.id.tv_SignupText)
        val text = "Sign up on the web at "+"<font color=#9400d3>www.flmly.com</font>"
        tvSignupText.setText(Html.fromHtml(text))



        Log.d("player","Url"+videoUrl)

        tvTitle.text = intent.getStringExtra("title")
        tvHeader.text =" "+ intent.getStringExtra("heading")
        duration = intent.getIntExtra("showTime",0)
        tvdetails_sub_Back.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadSubscription()

        initializePlayer()

        if (duration>0)
        {
            handlerTask.run()
            Log.d("zzz","inside resume")
        }
        else{
            Log.d("zzz","inside resume else")


        }

    }
    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {

        var mediaSource: MediaSource
        var dataSourceFactory: DataSource.Factory
        val uriOfContentUrl = Uri.parse(videoUrl)
        val mediaSourceType: Int = Util.inferContentType(uriOfContentUrl)

        when (mediaSourceType) {
            C.TYPE_DASH -> {
                // Create a data source factory.
                dataSourceFactory = DefaultHttpDataSourceFactory()
                // Create a DASH media source pointing to a DASH manifest uri.
                mediaSource = DashMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uriOfContentUrl))
                // Create a player instance.
                exoPlayer = SimpleExoPlayer.Builder(this).build()
                // Set the media source to be played.
                exoPlayer!!.setMediaSource(mediaSource)
                // Prepare the player.
                exoPlayer!!.prepare()
                progressBar.visibility= View.GONE
            }
            C.TYPE_SS -> {
                // Create a data source factory.
                dataSourceFactory = DefaultHttpDataSourceFactory()
                // Create a SmoothStreaming media source pointing to a manifest uri.
                mediaSource = SsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uriOfContentUrl))
                // Create a player instance.
                exoPlayer = SimpleExoPlayer.Builder(this).build()
                // Set the media source to be played.
                exoPlayer!!.setMediaSource(mediaSource)
                // Prepare the player.
                exoPlayer!!.prepare()
                progressBar.visibility= View.GONE
            }
            C.TYPE_HLS -> {


                // Create a data source factory.
                dataSourceFactory = DefaultHttpDataSourceFactory()

//                dataSourceFactory.defaultRequestProperties.set("Cookie",cookies)
                // Create a HLS media source pointing to a playlist uri.
                val hlsMediaSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uriOfContentUrl))
                // Create a player instance.
                exoPlayer = SimpleExoPlayer.Builder(this).build()
                // Set the media source to be played.
                exoPlayer!!.setMediaSource(hlsMediaSource)
                // Prepare the player.
                exoPlayer!!.prepare()
                progressBar.visibility= View.GONE
            }
            C.TYPE_OTHER -> {
                // Create a data source factory.
                dataSourceFactory = DefaultHttpDataSourceFactory()

//                Log.e("initializePlayer: ", videoUrl)
                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
                // Create a player instance.

//                Log.e("required url: ", "http://cfvod.kaltura.com/scf/pd/p/2704981/sp/270498100/serveFlavor/entryId/1_ijhpv0bl/v/1/ev/6/flavorId/1_nggv7ec0/name/a.mp4?Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cDovL2Nmdm9kLmthbHR1cmEuY29tL3NjZi9wZC9wLzI3MDQ5ODEvc3AvMjcwNDk4MTAwL3NlcnZlRmxhdm9yL2VudHJ5SWQvMV9pamhwdjBibC92LzEvZXYvNioiLCJDb25kaXRpb24iOnsiRGF0ZUxlc3NUaGFuIjp7IkFXUzpFcG9jaFRpbWUiOjE2MDk0MTQyMDZ9fX1dfQ__&Signature=C4EUQcuEDHWRiJbF5EXW11ZDgGnsM4bCpiM3Bc6k16a2j-DDgcDkk~xMRdfe4ukyb6u0vjISBjPv8ycmb0VWDxx35FeIdoAA637KPzlSzs3dpWctvYJN53O6A1TEfDn91mnldp93lWSoLOI2723M~6MhKV0D0DfqiIHZtzkV32Xnr7sA9XqWx1d5mt0TYXigF2z1v99MGq6HPJtVsqiI0ZleXDJhjrYTNo5Zs3JjMbTlSKNZYP6PW2fSc3bU3TudoZfGCKfQs3-UPJ~jXbYc9ztyLjrc112CDXnPV1hcdiF~oJoJYsd~vJA~qy6YuYzoUC69l9lif5KfOP78AeXA2g__&Key-Pair-Id=APKAJT6QIWSKVYK3V34A")
                exoPlayer = SimpleExoPlayer.Builder(this).build()

                // Set the media source to be played.
                exoPlayer!!.setMediaSource(mediaSource)
                // Prepare the player.
                exoPlayer!!.prepare()
                progressBar.visibility= View.GONE
            }
            else -> {
                dataSourceFactory = DefaultHttpDataSourceFactory()
                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uriOfContentUrl))
                exoPlayer = SimpleExoPlayer.Builder(this).build()
                exoPlayer!!.setMediaSource(mediaSource)
                exoPlayer!!.prepare()
                progressBar.visibility= View.GONE          }
        }

        // Create a player instance.
        playerView!!.player = exoPlayer


        this.exoPlayer?.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean,playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {}
                    Player.STATE_BUFFERING -> {
                        playerView?.showController()
                        progressBar.visibility=View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        playerView?.hideController()
                        progressBar.visibility=View.GONE

                    }
                    Player.STATE_ENDED -> {
                        stopTask()
                        pausePlayer()
                        releasePlayer()

//                        Handler().postDelayed({
//
                        finish()
//                        }, 500)
                    }
                }
            }
        })
        exoPlayer!!.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                Log.e("onPlayerError: ", error.toString())
            }

        })
        startPlayer()
    }

    private fun startPlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.playWhenReady = true
            exoPlayer!!.playbackState
        }
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            stopTask()
            exoPlayer!!.release()
            exoPlayer = null


        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        var handled = false
        when (keyCode) {
            20 -> if (keyCode == 20) {
                playerView?.showController()
                playerView?.setUseController(true)
                playerView?.setControllerShowTimeoutMs(5000)
            }
            23 -> if (keyCode == 23) {
                playerView?.showController()
                playerView?.setUseController(true)
                playerView?.setControllerShowTimeoutMs(5000)
            }
            KeyEvent.KEYCODE_MEDIA_REWIND -> {
                if (event.getAction() === 1) {
                    mIsLongPress = true
                    startContinualRewind()
                }
                handled = true
            }
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {

                playerView?.showController()
                playerView?.setUseController(true)
                playerView?.setControllerShowTimeoutMs(5000)
                handled = true
            }
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {
                if (event.getAction() === 1) {
                    mIsLongPress = true
                    startContinualFastForward()
                }
                handled = true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun startContinualFastForward() {
        playerView!!.showController()
        playerView!!.useController = true
        playerView!!.controllerShowTimeoutMs = 5000
        playerView!!.setFastForwardIncrementMs(30000)
    }

    private fun startContinualRewind() {
        playerView!!.showController()
        playerView!!.useController = true
        playerView!!.controllerShowTimeoutMs = 5000
        playerView!!.setRewindIncrementMs(30000)
    }

    override fun onPause() {

//        mTracker!!.setScreenName("${PlayerActivity::class.java.simpleName}, Exit from Video")
//        mTracker!!.send(HitBuilders.ScreenViewBuilder().build())
        super.onPause()
        pausePlayer()
    }
    fun stopTask() {
        Handler().removeCallbacks(handlerTask)
    }
    private fun pausePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.playWhenReady = false
            exoPlayer!!.playbackState
        }
    }

    fun loadSubscription() {
        try {
            progressBar.visibility = View.VISIBLE
            val stringRequest = object : StringRequest(
                    Method.GET,
                    Api.baseUrl + "settings/get_settings?from=tv&flmly_yearly_subscription_amount=1&flmly_monthly_subscription_amount=1",
                    Response.Listener { response ->
                        Log.e("zz", "$response")
                        val jsonObject = JSONObject(response)
                        val js2 = jsonObject.getJSONObject("data")
//                        tvSubscribe.text="Subscribe for $"+js2.getString("flmly_monthly_subscription_amount")+" /month"
                        tvdetailssignupPrice.text =
                                "Unlimited viewing for $" + js2.getString("flmly_monthly_subscription_amount") + " /month"
                        progressBar.visibility = View.GONE
                    },
                    Response.ErrorListener {
                        Toast.makeText(
                                applicationContext,
                                "Something went wrong.",
                                Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    }) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["api-key"] = "6vQKnb_G5c6"
                    return headers
                }
            }
            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
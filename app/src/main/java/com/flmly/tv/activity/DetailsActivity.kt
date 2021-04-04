package com.flmly.tv.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.flmly.tv.R
import com.flmly.tv.VolleySingleton
import com.flmly.tv.model.DetailsModel
import com.flmly.tv.utility.Api
import com.google.android.exoplayer2.ui.PlayerView
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.collections.HashMap


class DetailsActivity : Activity() {
    lateinit var directerImage: ImageView
    lateinit var banner: ImageView
    lateinit var tvDetailsTitle: AppCompatTextView
    lateinit var tvDetailsDirecterAge: AppCompatTextView
    var promoUrl: String = ""
    var videoUrl: String = ""
    var details_series_id:String=""
    lateinit var shaddow: ImageView
    lateinit var rettingLayout: ConstraintLayout
    lateinit var tvDetailsDirectorName: AppCompatTextView
    //    lateinit var tvdetailssignupPrice: AppCompatTextView
    lateinit var tvDetailsheading: AppCompatTextView
    lateinit var tvdetailsDesc: AppCompatTextView
    lateinit var tvdetailsGen: AppCompatTextView
    lateinit var tvYear: AppCompatTextView
    lateinit var smalldetailsBadge:ImageView
    lateinit var smalldetailsCerti:ImageView
    lateinit var tvDetailsDuration: AppCompatTextView
    lateinit var detailstvretting: AppCompatTextView
    lateinit var tvCast: AppCompatTextView
    lateinit var tvdetailsPlay: TextView
    lateinit var tvtrailer: TextView
    lateinit var tvrate: TextView
    lateinit var ratting: RatingBar
    lateinit var rateLayout: LinearLayout
    lateinit var playerLayout: ConstraintLayout
    var playerView: PlayerView? = null

    lateinit var progressBar: ProgressBar
    lateinit var starImage1: ImageView
    lateinit var starImage2: ImageView
    lateinit var starImage3: ImageView
    lateinit var starImage4: ImageView
    lateinit var starImage5: ImageView
    var smallCerti:String=""
    var smallbadge:String=""

    var sb = StringBuilder()
    var itemList: String = ""
    var rate: Int =0
    var duration: Int = 0
    var episode_id: String = ""
    var tempMessage: Int = 0
    var subMessage: String = ""
    var series_id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        inIt()

    }

    override fun onResume() {

//        Log.d("zz", "token:   " + HomeActivity.auth)
//        if (HomeActivity.auth.isNullOrEmpty()) {
//            tvrate.visibility = View.GONE
//        } else {
//            tvrate.visibility = View.VISIBLE
//        }
        super.onResume()
    }

    fun inIt() {
//        rate=0.0
        Log.d("zz","initRate:    "+rate)
        tvDetailsTitle = findViewById(R.id.tvDetailsTitle)
        directerImage = findViewById(R.id.detailsDirectorImage)
        banner = findViewById(R.id.mainImage)
        tvDetailsDirectorName = findViewById(R.id.tvDetailsDirectorName)
        tvDetailsheading = findViewById(R.id.tvDetailsheading)
        tvdetailsDesc = findViewById(R.id.tvdetailsDesc)
        tvDetailsDirecterAge=findViewById(R.id.tvdetailsDirectAge)
        tvdetailsGen = findViewById(R.id.tvdetailsGen)
        tvYear = findViewById(R.id.tvYear)
        tvDetailsDuration = findViewById(R.id.tvDetailsDuration)
        detailstvretting = findViewById(R.id.detailstvretting)
        smalldetailsBadge=findViewById(R.id.imgdetailsbadge)
        smalldetailsCerti=findViewById(R.id.imgdetailsCerti)
        tvCast = findViewById(R.id.tvCast)
        tvdetailsPlay = findViewById(R.id.tvdetailsPlay)
        tvtrailer = findViewById(R.id.tvtrailer)
        tvrate = findViewById(R.id.tvrate)
        ratting = findViewById(R.id.detailsrettingBar)
        progressBar = findViewById(R.id.detailsProgress)
        shaddow = findViewById(R.id.shadow)
        starImage1 = findViewById(R.id.oneStar)
        starImage2 = findViewById(R.id.twoStars)
        starImage3 = findViewById(R.id.threeStars)
        starImage4 = findViewById(R.id.fourStars)
        starImage5 = findViewById(R.id.fiveStars)
        rettingLayout = findViewById(R.id.rettingLayout)
        rateLayout = findViewById(R.id.rateLayout)

        val stars = ratting.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.parseColor("#DC8F26"), PorterDuff.Mode.SRC_IN)
//        loadSubscription()
        rateLayout.setOnClickListener {
            addRatting()
        }

        tvrate.setOnClickListener {
            rettingLayout.visibility = View.VISIBLE
            tvrate.clearFocus()
            rateLayout.requestFocus()
//            feedbackRatting.requestFocus()
        }

        series_id = intent.getStringExtra("series_id").toString()
        intent.getStringExtra("series_id")?.let { loadDetailsData(it) }

        tvtrailer.setOnClickListener {
            val intent = Intent(applicationContext, PlayerActivity::class.java)
            intent.putExtra("promoUrl", promoUrl)
            intent.putExtra("heading", tvDetailsheading.text)
            intent.putExtra("title", tvDetailsTitle.text)
            intent.putExtra("showTime", 0)
            intent.putExtra("sub", "")
            startActivity(intent)
        }

        tvdetailsPlay.setOnClickListener {

            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork?.isConnected!=null) {
                    // do your stuff. We have internet.
                    getSubscriptionStatus(episode_id)
                } else {
                    Toast.makeText(
                            applicationContext,
                            "Check your internet connection",
                            Toast.LENGTH_SHORT
                    ).show()
                    // We have no internet connection.
                }
//
//            getSubscriptionStatus(episode_id)
            Log.e("dd", "episode id" + episode_id)
        }


    }



    fun loadDetailsData(series_id: String) {
        try {
            val stringRequest = object : StringRequest(
                Method.GET,
                Api.baseUrl + "episode/viewers_series_episode_info/" + series_id + "?from=tv",
                Response.Listener { response ->
                    Log.d("bbbb", "details" + "$response" )

                    Log.d("bbbb", "details" + "series id" + series_id)
                    progressBar.visibility = View.VISIBLE
                    val jsonObject = JSONObject(response)
                    val js2 = jsonObject.getJSONObject("data")
                    Glide.with(applicationContext).load(Api.baseUrl2 + js2.getString("tv_thumb"))
                        .into(banner)
                    val jsonArrayepisodes = js2.getJSONArray("episodes")
                    for (i in 0 until jsonArrayepisodes.length()) {
                        val jsonObjectEpisode = jsonArrayepisodes.getJSONObject(i)
                        val contentModel = DetailsModel()
                        contentModel.duration = jsonObjectEpisode.getString("duration")
                        tvDetailsDuration.text =
                            "\u2022 " + (jsonObjectEpisode.getInt("duration") / 60).toInt() + "m"


                        Log.d("token", "auth tokn " + HomeActivity.auth+"status "+ jsonObjectEpisode.getString("status"))
                        if (jsonObjectEpisode.getString("status").equals("PUBLISHED") ) {
                            Log.d("checkccc","11"+"status  : "+jsonObjectEpisode.getString("status"))
                            tvdetailsPlay.visibility = View.VISIBLE
                            tvdetailsPlay.requestFocus()
                            if (HomeActivity.auth.isNullOrEmpty())
                            {
                                tvrate.visibility=View.GONE
                            }
                            else{
                                tvrate.visibility=View.VISIBLE

                            }
                        }
                        else if (jsonObjectEpisode.getString("status").equals("BETA_PUBLISHED") && js2.getString("is_beta_user").equals("true")) {
                            Log.d("checkccc","22"+"status:    "+ jsonObjectEpisode.getString("status")+"is beta check:   "+js2.getString("is_beta_user").equals("true"))
                            tvdetailsPlay.visibility = View.VISIBLE
                            tvrate.visibility=View.GONE
                            tvdetailsPlay.requestFocus()
                        } else {
                            Log.d("checkccc","33")

                            tvdetailsPlay.visibility = View.GONE
                        }
                        if (jsonObjectEpisode.has("s3_tv_file")) {
                            contentModel.s3_file = jsonObjectEpisode.getString("s3_tv_file")
                            videoUrl = Api.baseUrl2 + jsonObjectEpisode.getString("s3_tv_file")
                        } else {
                            videoUrl = Api.baseUrl2 + jsonObjectEpisode.getString("file")
                        }

                        if (jsonObjectEpisode.has("featured_times")) {
                            val jsonObjectfeatured_times =
                                jsonObjectEpisode.getJSONObject("featured_times")

                        }
                        contentModel.sub_status = jsonObjectEpisode.getString("sub_status")
                        if (jsonObjectEpisode.has("portrait_thumb")) {
                            contentModel.portrait_thumb =
                                jsonObjectEpisode.getString("portrait_thumb")
                        } else {
                            contentModel.portrait_thumb = ""
                        }
                        if (jsonObjectEpisode.has("is_landscape_mode_title_hide")) {
                            contentModel.is_landscape_mode_title_hide =
                                jsonObjectEpisode.getString("is_landscape_mode_title_hide")
                        } else {
                            contentModel.is_landscape_mode_title_hide = ""
                        }
                        if (jsonObjectEpisode.has("is_portrait_mode_title_hide")) {
                            contentModel.is_portrait_mode_title_hide =
                                jsonObjectEpisode.getString("is_portrait_mode_title_hide")
                        } else {
                            contentModel.is_portrait_mode_title_hide = ""
                        }
                        if (jsonObjectEpisode.has("promo_file")) {
                            Log.d("zzz", "Trailer check if")
                            if (jsonObjectEpisode.getString("promo_file") == "null" || jsonObjectEpisode.getString(
                                    "promo_file"
                                ) == ""
                            ) {
                                tvtrailer.visibility = View.GONE
                            } else {
                                contentModel.promo_file =
                                    jsonObjectEpisode.getString("promo_file")
                                promoUrl =
                                    Api.baseUrl2 + jsonObjectEpisode.getString("promo_file")
                                tvtrailer.visibility = View.VISIBLE
                                if (tvdetailsPlay.visibility==View.VISIBLE)
                                {
                                    tvdetailsPlay.requestFocus()
                                }
                                else {
                                    tvtrailer.requestFocus()
                                }
                            }
                        } else {
                            tvtrailer.visibility = View.GONE
                            Log.d("zzz", "Trailer check else")
                        }

                        contentModel.publish_year = jsonObjectEpisode.getString("publish_year")
                        tvYear.text = "\u2022 " + jsonObjectEpisode.getString("publish_year")
                        Log.d("zzz","episode id details"+jsonObjectEpisode.getString("episode_id"))
                        episode_id = jsonObjectEpisode.getString("episode_id")
                        contentModel.my_rating = jsonObjectEpisode.getString("my_rating")
                        rate = jsonObjectEpisode.getInt("my_rating")

                        Log.d("cc","Episode_myRatting:  "+jsonObjectEpisode.getDouble("my_rating"))
                        val jsonObjectepisode_credits_info =
                            jsonObjectEpisode.getJSONObject("episode_credits_info")
                        val jsonArrayactors =
                            jsonObjectepisode_credits_info.getJSONArray("actors")
                        if (jsonArrayactors.length() >= 3) {
                            for (j in 0 until 2) {
                                val jsonObjectActors = jsonArrayactors.getJSONObject(j)
                                for (s in Arrays.asList(
                                    jsonObjectActors.getString("value")
                                )) {
                                    if (s != null) {
                                        sb.append(s).append(" ")
                                    }
                                }
                                itemList = sb.toString()
                                Log.d("zzz", "itemList:  " + itemList)
                            }
                            sb.clear()

                        } else {
                            for (j in 0 until jsonArrayactors.length()) {
                                val jsonObjectActors = jsonArrayactors.getJSONObject(j)
                                for (s in Arrays.asList(
                                    jsonObjectActors.getString("value")
                                )) {
                                    if (s != null) {
                                        sb.append(s).append(" ")
                                    }
                                }
                                itemList = sb.toString()
                                Log.d("zzz", "itemList:  " + itemList)
                            }
                            sb.clear()

                        }
                        tvCast.text = "Cast: " + itemList
                        tvDetailsheading.text =
                            "By the " + js2.getString("user_lname") + " family"
                        contentModel.user_thumb = js2.getString("user_thumb")
                        contentModel.user_age = js2.getString("user_age")

                        tvDetailsDirectorName.text = js2.getString("director_name")
                        Glide.with(applicationContext)
                            .load(Api.baseUrl2 + "/uploads" + js2.getString("director_thumb"))
                            .into(directerImage)
                        if (js2.has("director_age")) {
                            contentModel.director_age = js2.getString("director_age")
                            if (js2.getString("director_age")!="") {
                                tvDetailsDirecterAge.text = "Age" + " " + js2.getString("director_age")
                            }
                            else{
                                tvDetailsDirecterAge.text =""
                            }
                        }
                        contentModel.rating = js2.getString("rating")
                        if (js2.getString("rating").length == 1) {
                            detailstvretting.text = js2.getString("rating") + ".0"
                        } else {
                            detailstvretting.text = js2.getString("rating")
                        }
                        ratting.rating = js2.getDouble("rating").toFloat()

                        Log.d("zz","rate:    "+js2.getDouble("rating").toFloat())
                        Log.d("cc","outside_myRatting:  "+js2.getString("my_rating"))
                        details_series_id=js2.getString("series_id")

                        if (js2.getInt("my_rating")>0) {
                            if (js2.getInt("my_rating") == 1) {
                                rate=1
                                starImage1.setImageResource(R.drawable.yellow_star);
                            }
                            else if (js2.getInt("my_rating") == 2)
                            {
                                rate=2
                                starImage1.setImageResource(R.drawable.yellow_star);
                                starImage2.setImageResource(R.drawable.yellow_star);

                            }
                            else if (js2.getInt("my_rating") == 3)
                            {
                                rate=3
                                starImage1.setImageResource(R.drawable.yellow_star);
                                starImage2.setImageResource(R.drawable.yellow_star);
                                starImage3.setImageResource(R.drawable.yellow_star);
                            }
                            else if (js2.getInt("my_rating") == 4)
                            {
                                rate=4
                                starImage1.setImageResource(R.drawable.yellow_star);
                                starImage2.setImageResource(R.drawable.yellow_star);
                                starImage3.setImageResource(R.drawable.yellow_star);
                                starImage4.setImageResource(R.drawable.yellow_star);
                            }
                            else if (jsonObjectEpisode.getInt("my_rating") == 5)
                            {
                                rate=5
                                starImage1.setImageResource(R.drawable.yellow_star);
                                starImage2.setImageResource(R.drawable.yellow_star);
                                starImage3.setImageResource(R.drawable.yellow_star);
                                starImage4.setImageResource(R.drawable.yellow_star);
                                starImage5.setImageResource(R.drawable.yellow_star);

                            }
                        }


//                                feedbackRatting.rating=js2.getDouble("rating").toFloat()

                        tvDetailsTitle.text = js2.getString("title")
                       tvdetailsDesc.text = js2.getString("synopsis")
                        contentModel.genre = js2.getString("genre")
                        tvdetailsGen.text = js2.getString("genre")
//                                contentModel.child_user_id=js2.getString("child_user_id")
                        if (js2.has("portrait_thumb")) {
                            contentModel.portrait_thumb = js2.getString("portrait_thumb")
                        } else {
                            contentModel.portrait_thumb = ""
                        }

                        if (js2.has("is_landscape_mode_title_hide")) {
                            contentModel.is_landscape_mode_title_hide =
                                js2.getString("is_landscape_mode_title_hide")
                        } else {
                            contentModel.is_landscape_mode_title_hide = ""
                        }

                        if (js2.has("is_portrait_mode_title_hide")) {
                            contentModel.is_portrait_mode_title_hide =
                                js2.getString("is_portrait_mode_title_hide")
                        } else {
                            contentModel.is_portrait_mode_title_hide = ""
                        }
                        if (js2.has("is_audition")) {
                            contentModel.is_audition = js2.getString("is_audition")
                        } else {
                            contentModel.is_audition = ""
                        }
                        val jsonArrayusers = js2.getJSONArray("users")
                        for (j in 0 until jsonArrayusers.length()) {
                            val jsonObjectuser = jsonArrayusers.getJSONObject(j)

                        }

                        if (js2.has("is_tutorial_badge_earned")) {

                            smallbadge=js2.getString("is_tutorial_badge_earned")

                        }
                        if (js2.has("is_tutorial_certificate_earned"))
                        {

                            smallCerti=js2.getString("is_tutorial_certificate_earned")

                        }

                        if (smallbadge=="true"&& smallCerti=="true")
                        {
                            smalldetailsBadge.visibility=View.VISIBLE
                            smalldetailsCerti.visibility=View.GONE

                        }
                        else if (smallCerti=="true")
                        {
                            smalldetailsBadge.visibility=View.GONE
                            smalldetailsCerti.visibility=View.VISIBLE

                        }
                        else if (smallbadge=="true")
                        {
                            smalldetailsCerti.visibility=View.GONE
                            smalldetailsBadge.visibility=View.VISIBLE
                        }

                        progressBar.visibility = View.GONE
                        shaddow.visibility = View.GONE
                    }
                }, Response.ErrorListener {
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
                    headers["x-auth-token"] = HomeActivity.auth
                    return headers
                }
            }
            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                super.onKeyDown(keyCode, event)
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (rateLayout.hasFocus()) {
                    if (rate > 0) {
                        rate--
                        Log.d("zz", "rate " + rate)
                        if (rate == 4) {
                            starImage5.setImageResource(R.drawable.white_star);
                        } else if (rate == 3) {
                            starImage4.setImageResource(R.drawable.white_star)
                        } else if (rate == 2) {
                            starImage3.setImageResource(R.drawable.white_star)
                        } else if (rate == 1) {
                            starImage2.setImageResource(R.drawable.white_star)
                        } else if (rate == 0) {
                            starImage1.setImageResource(R.drawable.white_star)
//                        rate = 0
                        }

                    }
                }
                super.onKeyDown(keyCode, event)
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (rateLayout.hasFocus()) {
                    if (rate <= 4) {
                        rate++
                        Log.d("zz", "rate " + rate)
                        if (rate == 1) {
                            starImage1.setImageResource(R.drawable.yellow_star);
                        } else if (rate == 2) {
                            starImage2.setImageResource(R.drawable.yellow_star);
                        } else if (rate == 3) {
                            starImage3.setImageResource(R.drawable.yellow_star);
                        } else if (rate == 4) {
                            starImage4.setImageResource(R.drawable.yellow_star);
                        } else if (rate == 5) {
                            starImage5.setImageResource(R.drawable.yellow_star);
                        }
                    }
                }

                super.onKeyDown(keyCode, event)

            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                super.onKeyDown(keyCode, event)

            }
            KeyEvent.KEYCODE_BACK -> {
                if (rateLayout.hasFocus()) {
                    addRatting()
//                    } else if (subscribe_details_Layout.visibility == View.VISIBLE) {
//                        subscribe_details_Layout.visibility = View.GONE
//                    } else if (layout_signup_details.visibility == View.VISIBLE) {
//                        layout_signup_details.visibility = View.GONE
                } else {
                    super.onBackPressed()
                }
                return true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }


    fun getSubscriptionStatus(id: String) {
        try {
//            HomeActivity.progressBar.visibility = View.VISIBLE
            val stringRequest = object : StringRequest(
                Method.GET,
                "https://www.flmly.com/users/get_user_subscription_status?episode_id="+id,
                Response.Listener { response ->
                    Log.e("ddd", "$response")
                    val jsonObject = JSONObject(response)
                    subMessage = jsonObject.getString("data")

                    Log.d("vv","data"+jsonObject.getString("data"))
                    if (jsonObject.getString("data").equals("SUBSCRIBED")) {
                        Log.e("ddd", "detals inside subscribed")
                        val intent = Intent(applicationContext, PlayerActivity::class.java)
                        intent.putExtra("promoUrl", videoUrl)
                        intent.putExtra("heading", tvDetailsheading.text)
                        intent.putExtra("showTime", 0)
                        intent.putExtra("title", tvDetailsTitle.text)
                        intent.putExtra("sub", "")
                        startActivity(intent)
                    } else {
                        Log.e("ddd", "detals inside else")
                        getUnubscriptionStatus()
                    }

                },
                Response.ErrorListener {
                Toast.makeText(
                        applicationContext,
                        "Check your internet connection.",
                        Toast.LENGTH_SHORT
                ).show()
//                HomeActivity.progressBar.visibility = View.GONE
                }) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["api-key"] = "6vQKnb_G5c6"
                    headers["x-auth-token"] = HomeActivity.auth
                    return headers
                }
            }

            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun getUnubscriptionStatus() {
        try {
            val stringRequest = object : StringRequest(
                Method.GET,
                "https://www.flmly.com/api/settings/get_settings?from=tv&unsubscribe_user_film_watch_count=1&unsubscribe_user_film_watch_time=1",
                Response.Listener { response ->
                    Log.e("ddd", "$response")
                    val jsonObject = JSONObject(response)
                    val jsonObjectdata = jsonObject.getJSONObject("data")
                    duration = jsonObjectdata.getInt("unsubscribe_user_film_watch_time")
                    val intent = Intent(applicationContext, PlayerActivity::class.java)
                    intent.putExtra("promoUrl", videoUrl)
                    intent.putExtra("heading", tvDetailsheading.text)
                    intent.putExtra("showTime", duration)
                    intent.putExtra("title", tvDetailsTitle.text)
                    intent.putExtra("sub", subMessage)
//                        tempMessage = 2
                    startActivity(intent)

                },
                Response.ErrorListener {
                    Toast.makeText(
                        applicationContext,
                        "Check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["api-key"] = "6vQKnb_G5c6"
                    headers["x-auth-token"] = HomeActivity.auth
                    return headers
                }
            }
            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun addRatting() {
        progressBar.visibility = View.VISIBLE
        val jsonObject1 = JSONObject()
        jsonObject1.put("series_id", details_series_id)
        jsonObject1.put("rating", rate)

        Log.d("cc","ratting:   "+rate+"    Series _id:  "+details_series_id )
        val requestBody = jsonObject1.toString()
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            Api.baseUrl + "ratings/add_series_rating?from=tv",
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("cc","response add ratting:   "+response)

                    if (jsonObject.has("status")) {
//                    Toast.makeText(applicationContext, "" + jsonObject.getString("status"), Toast.LENGTH_SHORT).show()
//                        inIt()
                        progressBar.visibility = View.GONE
                        rettingLayout.visibility = View.GONE
                        tvrate.requestFocus()

                        }
                    val js2 = jsonObject.getJSONObject("data")
//                    recreate()
                    inIt()


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e("VOLLEY", error.toString())
//            Log.d("status  ",""+mStatusCode)
                progressBar.visibility = View.GONE
                rettingLayout.visibility = View.GONE
                tvrate.requestFocus()
                Toast.makeText(
                        applicationContext,
                        "Check your internet connection",
                        Toast.LENGTH_SHORT
                ).show()

            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                if (response != null) {
//                    mStatusCode = response.statusCode
                }
                return super.parseNetworkResponse(response)
//                    Log.d("zz","Login response"+mStatusCode)
//
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    requestBody.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    VolleyLog.wtf(
                        "Unsupported Encoding while trying to get the bytes of %s using %s",
                        requestBody,
                        "utf-8"
                    )
                    return null
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val headers =
                    HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["api-key"] = "6vQKnb_G5c6"
                headers["x-auth-token"] = HomeActivity.auth
                return headers
            }
        }
        stringRequest.setShouldCache(false)
        applicationContext?.let {
            VolleySingleton.getInstance(it).addToRequestQueue(stringRequest)
        }
    }

}


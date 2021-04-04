package com.flmly.tv.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.SearchEditText
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.flmly.tv.R
import com.flmly.tv.VolleySingleton
import com.flmly.tv.fragment.HomeFragment
import com.flmly.tv.fragment.SearchFragment
import com.flmly.tv.model.DetailsModel
import com.flmly.tv.model.LoginModel
import com.flmly.tv.utility.Api
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.UnsupportedEncodingException


class HomeActivity : FragmentActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var layout: ConstraintLayout
    lateinit var layoutSetting: ConstraintLayout
    lateinit var etEmail: AppCompatEditText
    lateinit var etPass: AppCompatEditText
    lateinit var tvContineu: TextView
    lateinit var textHomeTc: TextView
    lateinit var tvcancle: AppCompatTextView
    lateinit var textHomepp: TextView
    lateinit var logout: AppCompatTextView
    lateinit var tvLogoutConti: AppCompatTextView
    lateinit var tcScrollView: ScrollView
    lateinit var tvcontent: AppCompatTextView
    lateinit var tvhomeTc: AppCompatTextView
    lateinit var layoutHometc: ConstraintLayout
    lateinit var logoutDialog: ConstraintLayout
    var videoUrl: String = ""
    var duration: Int = 0
    var subMessage: String = ""
    var headingText: String = ""
    var play_title: String = ""
    var tempsearchPos: String = ""


    var data: String = ""
    var text_tc: String = ""
    var text_pp: String = ""
    val emailPattern =
        Regex("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    companion object {
        var focus: Int = 0
        var indexOfRow: Int = 0
        var searchRow: Int = 0
        var series_id: String = ""
        var episode_id: String = ""
        var searchData: String = ""
        var tempSearchValue: String = ""
        var homeFragment: HomeFragment? = null
        var searchFragment: SearchFragment? = null
        lateinit var imageBanner: ImageView
        lateinit var imgbadge: ImageView
        lateinit var smallcertificate: ImageView
        lateinit var imgOverlay: ImageView
        lateinit var profileImage: ImageView
        lateinit var imageDir: ImageView
        lateinit var imageShado: ImageView
        lateinit var searchEditText: AppCompatEditText
        lateinit var movieNotFoundText: AppCompatTextView
        lateinit var tvSetting: AppCompatTextView
        lateinit var tvHome: AppCompatTextView
        lateinit var tvSearch: AppCompatTextView
        lateinit var tvSignIn: AppCompatTextView
        lateinit var tvProfileName: AppCompatTextView
        lateinit var tvTitle: TextView
        lateinit var tvheader: TextView
        lateinit var tvDesc: TextView
        lateinit var tvDirectAge: TextView
        lateinit var progressBar: ProgressBar
        lateinit var tvGen: TextView
        lateinit var tvDur: TextView
        lateinit var tvratting: TextView
        lateinit var tvDirect: TextView
        lateinit var tvDirector: TextView
        lateinit var tvPlay: TextView
        lateinit var tvHomeSignupBack: TextView
        lateinit var tvMore: TextView
        lateinit var ratting: AppCompatRatingBar
        lateinit var btnHome: LinearLayout
        lateinit var btnSearch: LinearLayout
        lateinit var signIn: LinearLayout
        lateinit var setting: LinearLayout
        lateinit var profile: LinearLayout
        lateinit var auth: String
        lateinit var name: String
        lateinit var searchLayout: ConstraintLayout
        lateinit var image: String
        var title1: String = ""
        var header: String = ""
        var description: String = ""
        var genere: String = ""
        var bannerImageUrl: String = ""
        var id: String = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
        tvPlay.setOnClickListener {
            loadPlayButtonData(series_id)
//            getSubscriptionStatus(episode_id)
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        }


        tvMore.setOnClickListener {
            val intent = Intent(applicationContext, DetailsActivity::class.java)
            intent.putExtra("series_id", series_id)
            startActivity(intent)

        }

        btnHome.setOnClickListener {
            layoutSetting.visibility = View.GONE
            searchLayout.visibility = View.GONE
            searchData = "no"
            searchFragment!!.view!!.visibility = View.GONE
            movieNotFoundText.visibility = View.GONE
            layout.visibility = View.GONE
            homeFragment!!.view!!.requestFocus()
        }

        btnHome.setOnFocusChangeListener { view, b ->
            if (b) {
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvHome.setTypeface(type)
                tvHome.setTextSize(13f) //increased size

//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvHome.setTypeface(type)
                tvHome.setTextSize(11f)
//                tvPrivacy.setTextSize(12f) //normal size
            }
        }
        btnSearch.setOnFocusChangeListener { view, b ->
            if (b) {
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvSearch.setTextSize(13f) //increased size
                tvSearch.setTypeface(type)
            } else {
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvSearch.setTypeface(type)
                tvSearch.setTextSize(11f)

            }
        }
        btnSearch.setOnClickListener {
            searchLayout.visibility = View.VISIBLE
            layoutSetting.visibility = View.GONE
            searchFragment!!.view!!.visibility = View.GONE
            movieNotFoundText.visibility = View.GONE
            searchData = "no"
            tempsearchPos="kickFocus"
            searchEditText.requestFocus()
        }




        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > 0) {
//                    searchLayout.visibility = View.VISIBLE

                    searchFragment!!.loadData(charSequence.toString().trim())
                } else {
                    searchFragment!!.view!!.visibility = View.GONE
                    searchData="no"
//                    searchLayout.visibility=View.GONE
                    searchEditText.requestFocus()
//                    Toast.makeText(applicationContext, "Type to search", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        signIn.setOnClickListener {
            layout.visibility = View.VISIBLE
            layoutSetting.visibility = View.GONE
            searchLayout.visibility = View.GONE
            searchFragment!!.view!!.visibility = View.GONE
            movieNotFoundText.visibility = View.GONE
            searchData = "no"
            etEmail.requestFocus()
        }

//        tvContineu.setOnFocusChangeListener { view, b ->
////            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
////            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
//        }
        tvContineu.setOnClickListener {
            checkSignUpEdittxt()
//            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)


        }

        setting.setOnClickListener {
            layoutSetting.visibility = View.VISIBLE
            layout.visibility = View.GONE
            searchLayout.visibility = View.GONE
            searchFragment!!.view!!.visibility = View.GONE
            movieNotFoundText.visibility = View.GONE
            searchData = "no"
            textHomeTc.requestFocus()
        }

        signIn.setOnFocusChangeListener { view, b ->
            if (b) {
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvSignIn.setTextSize(13f) //increased size
                tvSignIn.setTypeface(type)
//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvSignIn.setTypeface(type)
                tvSignIn.setTextSize(11f) //normal size
            }
        }
        setting.setOnFocusChangeListener { view, b ->
            if (b) {
//                tvPrivacy.setTextSize(14f) //increased size
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvSetting.setTypeface(type)
                tvSetting.setTextSize(13f)
//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvSetting.setTypeface(type)
                tvSetting.setTextSize(11f)
//                tvPrivacy.setTextSize(12f) //normal size
            }
        }
        profile.setOnFocusChangeListener { view, b ->
            if (b) {
//                tvPrivacy.setTextSize(14f) //increased size
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvProfileName.setTypeface(type)
                tvProfileName.setTextSize(13f)

//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvProfileName.setTypeface(type)
                tvProfileName.setTextSize(11f)

//                tvPrivacy.setTextSize(12f) //normal size
            }
        }

        textHomeTc.setOnFocusChangeListener { view, b ->
            if (b) {
//                tvPrivacy.setTextSize(14f) //increased size
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                textHomeTc.setTypeface(type)
                textHomeTc.setTextSize(15f)
//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                textHomeTc.setTypeface(type)
                textHomeTc.setTextSize(13f)
//                tvPrivacy.setTextSize(12f) //normal size
            }
        }

        textHomepp.setOnFocusChangeListener { view, b ->
            if (b) {
//                tvPrivacy.setTextSize(14f) //increased size
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                textHomepp.setTypeface(type)
                textHomepp.setTextSize(15f)
//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                textHomepp.setTypeface(type)
                textHomepp.setTextSize(13f)
//                tvPrivacy.setTextSize(12f) //normal size
            }
        }


        logout.setOnFocusChangeListener { view, b ->
            if (b) {
//                tvPrivacy.setTextSize(14f) //increased size
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                logout.setTypeface(type)
                logout.setTextSize(15f)
//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                logout.setTypeface(type)
                logout.setTextSize(13f)
//                tvPrivacy.setTextSize(12f) //normal size
            }
        }


        tvLogoutConti.setOnFocusChangeListener { view, b ->
            if (b) {
//                tvPrivacy.setTextSize(14f) //increased size
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvLogoutConti.setTypeface(type)
                tvLogoutConti.setTextSize(15f)
//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvLogoutConti.setTypeface(type)
                tvLogoutConti.setTextSize(13f)
//                tvPrivacy.setTextSize(12f) //normal size
            }
        }


        tvcancle.setOnFocusChangeListener { view, b ->
            if (b) {
//                tvPrivacy.setTextSize(14f) //increased size
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvcancle.setTypeface(type)
                tvcancle.setTextSize(15f)
//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvcancle.setTypeface(type)
                tvcancle.setTextSize(13f)
//                tvPrivacy.setTextSize(12f) //normal size
            }
        }

        textHomeTc.setOnClickListener {
            data = "1"
            tvcontent.setText("")
            tcScrollView.requestFocus()
//            tvhomeTc.setText("Terms and conditions")
            tcScrollView.fullScroll(ScrollView.FOCUS_UP)
            layoutHometc.visibility = View.VISIBLE
            tvcontent.text = Jsoup.parse(text_tc).wholeText()
        }
        textHomepp.setOnClickListener {
            data = "2"
            tvcontent.setText("")
            tcScrollView.requestFocus()
//            tvhomeTc.setText("Privacy Policy")
            tcScrollView.fullScroll(ScrollView.FOCUS_UP)
            layoutHometc.visibility = View.VISIBLE
//            Log.d("zz", "pp" + Html.fromHtml(text_pp))
            tvcontent.text = Jsoup.parse(text_pp).wholeText()

        }

        logout.setOnClickListener {
            logoutDialog.visibility = View.VISIBLE
            tvLogoutConti.requestFocus()
        }

        tvLogoutConti.setOnClickListener {
            val preferences = getSharedPreferences("authData", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
            auth = ""
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        tvcancle.setOnClickListener {
            logoutDialog.visibility = View.GONE
            logout.requestFocus()
        }


    }


    fun loadPlayButtonData(series_id: String) {
        try {
            val stringRequest = object : StringRequest(
                Method.GET,
                Api.baseUrl + "episode/viewers_series_episode_info/" + series_id + "?from=tv",
                Response.Listener { response ->
                    Log.d("bbbb", "homepagePlay" + "$response" + "series id" + series_id)
                    progressBar.visibility = View.VISIBLE
                    val jsonObject = JSONObject(response)
                    val js2 = jsonObject.getJSONObject("data")
                    val jsonArrayepisodes = js2.getJSONArray("episodes")
                    for (i in 0 until jsonArrayepisodes.length()) {
                        val jsonObjectEpisode = jsonArrayepisodes.getJSONObject(i)
                        val contentModel = DetailsModel()
//                            contentModel.duration = jsonObjectEpisode.getString("duration")
//                            contentModel.view_count = jsonObjectEpisode.getString("view_count")
//                            contentModel.status = jsonObjectEpisode.getString("status")

                        if (jsonObjectEpisode.getString("status").equals("PUBLISHED")) {

                        } else if (jsonObjectEpisode.getString("status")
                                .equals("BETA_PUBLISHED") && js2.getString("is_beta_user")
                                .equals("true")
                        ) {

                        } else {

                        }
                        play_title = jsonObjectEpisode.getString("title")
                        contentModel.description = jsonObjectEpisode.getString("description")
                        contentModel.series_id = jsonObjectEpisode.getString("series_id")
                        if (jsonObjectEpisode.has("s3_tv_file")) {
                            contentModel.s3_file = jsonObjectEpisode.getString("s3_tv_file")
                            videoUrl = Api.baseUrl2 + jsonObjectEpisode.getString("s3_tv_file")
                        } else {
                            videoUrl = Api.baseUrl2 + jsonObjectEpisode.getString("file")
                        }
                        contentModel.sub_status = jsonObjectEpisode.getString("sub_status")

                        if (jsonObjectEpisode.has("promo_file")) {
                            Log.d("zzz", "Trailer check if")
                            if (jsonObjectEpisode.getString("promo_file") == "null" || jsonObjectEpisode.getString(
                                    "promo_file"
                                ) == ""
                            ) {
                            } else {
                                contentModel.promo_file =
                                    jsonObjectEpisode.getString("promo_file")
                            }
                        } else {
//                                tvtrailer.visibility = View.GONE
                            Log.d("zzz", "Trailer check else")
                        }
                        contentModel.is_audition = jsonObjectEpisode.getString("is_audition")
                        contentModel.publish_year = jsonObjectEpisode.getString("publish_year")
                        episode_id = jsonObjectEpisode.getString("episode_id")
                        contentModel.my_rating = jsonObjectEpisode.getString("my_rating")
                        contentModel.thumb_size_1 = jsonObjectEpisode.getString("thumb_size_1")
                        contentModel.thumb_size_3 = jsonObjectEpisode.getString("thumb_size_3")
                        val jsonObjectepisode_credits_info =
                            jsonObjectEpisode.getJSONObject("episode_credits_info")
                        contentModel.user_fname = js2.getString("user_fname")
                        contentModel.user_lname = js2.getString("user_lname")
                        headingText = "By the " + js2.getString("user_lname") + " family"

                        contentModel.rating = js2.getString("rating")
                        if (js2.getString("rating").length == 1) {
                        } else {
                        }
                        ratting.rating = js2.getDouble("rating").toFloat()
                        contentModel.status_2 = js2.getString("status")
                        contentModel.is_featured = js2.getString("status")
                        contentModel.is_archived = js2.getString("status")
                        contentModel.type = js2.getString("type")
                        contentModel.title = js2.getString("title")
                        contentModel.synopsis = js2.getString("synopsis")
                        contentModel.genre = js2.getString("genre")
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
                    }

                    getSubscriptionStatus(episode_id)
                }, Response.ErrorListener {
                    Toast.makeText(
                        applicationContext,
                        "Check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["api-key"] = "6vQKnb_G5c6"
                    headers["x-auth-token"] = auth
                    return headers
                }
            }
            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    fun getSubscriptionStatus(id: String) {
        try {
            val stringRequest = object : StringRequest(
                Method.GET,
                "https://www.flmly.com/users/get_user_subscription_status?episode_id=" + id,
                Response.Listener { response ->
                    Log.e("ddd", "$response")
                    val jsonObject = JSONObject(response)
                    subMessage = jsonObject.getString("data")
                    Log.d("vv", "data" + jsonObject.getString("data"))
                    if (jsonObject.getString("data").equals("SUBSCRIBED")) {
                        Log.e("ddd", "detals inside subscribed")
                        val intent = Intent(applicationContext, PlayerActivity::class.java)
                        intent.putExtra("promoUrl", videoUrl)
                        intent.putExtra("heading", headingText)
                        intent.putExtra("showTime", 0)
                        intent.putExtra("title", play_title)
                        intent.putExtra("sub", "")
                        focus = 3
                        startActivity(intent)
                    } else {
                        Log.e("ddd", "detals inside else")
                        getUnubscriptionStatus()
                    }

                },
                Response.ErrorListener {
//                Toast.makeText(
//                        applicationContext,
//                        "Something went wrong.",
//                        Toast.LENGTH_SHORT
//                ).show()
                    progressBar.visibility = View.GONE
                }) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["api-key"] = "6vQKnb_G5c6"
                    headers["x-auth-token"] = auth
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
                    intent.putExtra("heading", headingText)
                    intent.putExtra("showTime", duration)
                    intent.putExtra("title", play_title)
                    intent.putExtra("sub", subMessage)
                    focus = 3
//                        tempMessage = 2
                    startActivity(intent)
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
                    headers["x-auth-token"] = auth
                    return headers
                }
            }
            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()

        if (focus == 1) {
            homeFragment!!.view!!.requestFocus()
        } else if (focus == 3) {
            tvPlay.requestFocus()
        }
        if (tempSearchValue.equals("searchFocus")) {
            searchFragment!!.view!!.requestFocus()
            movieNotFoundText.visibility = View.GONE
            tempSearchValue = ""
        }
        if (setting.hasFocus() && tempsearchPos=="kickFocus")
        {
            searchEditText.requestFocus()

        }
        else{

        }



    }

    fun init() {
        sharedPreferences = getSharedPreferences("authData", Activity.MODE_PRIVATE)
        if (sharedPreferences.getString("token", "").isNullOrEmpty()) {
            auth = ""
        } else {
            auth = sharedPreferences.getString("token", "")!!
        }

        name = sharedPreferences.getString("name", "")!!
        image = sharedPreferences.getString("image", "")!!
        Log.d("cc", "image" + image)
        layout = findViewById(R.id.loginHomeLayout)
        logoutDialog = findViewById(R.id.logoutDialog)
        searchLayout = findViewById(R.id.layout_search)
        tcScrollView = findViewById(R.id.tcHomeScroll)
        layoutHometc = findViewById(R.id.layoutHometc)
        tvLogoutConti = findViewById(R.id.tvLogoutConti)
        movieNotFoundText = findViewById(R.id.movieNotFoundMessage)
        layoutSetting = findViewById(R.id.layoutSetting)
        etEmail = findViewById(R.id.etHomeEmail)
        etPass = findViewById(R.id.etHomePass)
        tvcontent = findViewById(R.id.tvHomeContent)
        tvContineu = findViewById(R.id.tvHomeContinue)
        imageBanner = findViewById(R.id.img_banner)
        imgbadge = findViewById(R.id.imgbadge)
        smallcertificate = findViewById(R.id.smallCerti)
        tvTitle = findViewById(R.id.tvTitle)
        tvheader = findViewById(R.id.tvheading)
        tvDesc = findViewById(R.id.tvDesc)
        tvGen = findViewById(R.id.tvGen)
        textHomepp = findViewById(R.id.textHomepp)
        textHomeTc = findViewById(R.id.textHomeTc)
        logout = findViewById(R.id.textLogout)
        tvcancle = findViewById(R.id.tvcancle)
        tvDur = findViewById(R.id.tvDuration)
        tvratting = findViewById(R.id.tvretting)
        tvPlay = findViewById(R.id.tvPlay)
        imageDir = findViewById(R.id.directImg)
        imgOverlay = findViewById(R.id.imgOverlay)
        imageShado = findViewById(R.id.imageShado)
        progressBar = findViewById(R.id.homeProgress)
        tvMore = findViewById(R.id.tvMoreInfo)
        tvProfileName = findViewById(R.id.profileName)
        tvDirect = findViewById(R.id.tvDirectName)
        tvDirector = findViewById(R.id.tvDirector)
        ratting = findViewById(R.id.rettingBar)
        tvDirectAge = findViewById(R.id.tvDirectAge)
        btnHome = findViewById(R.id.btnHome)
        btnSearch = findViewById(R.id.btnSearch)
        signIn = findViewById(R.id.signIN)
        setting = findViewById(R.id.btnSettings)
        profile = findViewById(R.id.btnProfile)
        tvProfileName.text = name
        searchEditText = findViewById(R.id.editTextSearch)
        profileImage = findViewById(R.id.profileImage)
        tvHome = findViewById(R.id.tvHome)
        tvSetting = findViewById(R.id.tvSetting)
        tvSearch = findViewById(R.id.tvSearch)
        tvSignIn = findViewById(R.id.tvSignIn)
        tvProfileName = findViewById(R.id.profileName)

        if (name.isNullOrEmpty()) {
            profile.visibility = View.GONE
            signIn.visibility = View.VISIBLE
            logout.visibility = View.GONE
//            btnSearch.visibility=View.GONE
        } else {
            profile.visibility = View.VISIBLE
            signIn.visibility = View.GONE
            logout.visibility = View.VISIBLE
//            btnSearch.visibility = View.VISIBLE
            Glide.with(applicationContext).load("https://www.flmly.com/" + "/uploads" + image)
                .into(profileImage)
        }
        val stars = ratting.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.parseColor("#DC8F26"), PorterDuff.Mode.SRC_IN)
        loadPolicy()
        loadTc()
        searchFragment =
            supportFragmentManager.findFragmentById(R.id.searchFragment) as SearchFragment
        homeFragment = supportFragmentManager.findFragmentById(
            R.id.homeFragment
        ) as HomeFragment
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                Log.d("zzz", "indexOfRow" + indexOfRow)

                if (homeFragment!!.view!!.hasFocus() && indexOfRow == 1) {
                    Log.d("dmdd", "up 11")
                    btnHome.requestFocus()
                } else if (searchFragment!!.view!!.hasFocus() && searchRow <= 7) {
                    Log.d("dmdd", "up 22")
                    searchEditText.requestFocus()
                } else if (searchEditText.hasFocus()) {
                    Log.d("dmdd", "up 33")
                    btnSearch.requestFocus()
                } else if (layoutSetting.visibility == View.VISIBLE && textHomeTc.hasFocus()) {
                    Log.d("dmdd", "up 44")
                    setting.requestFocus()
                } else {
                    super.onKeyDown(keyCode, event)
                }
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                super.onKeyDown(keyCode, event)
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                super.onKeyDown(keyCode, event)

            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (setting.hasFocus()&& tempsearchPos=="kickFocus")
                {
//                    searchEditText.requestFocus()
                    super.onKeyDown(keyCode, event)

                }
                if (tvPlay.hasFocus() || tvMore.hasFocus()) {
                    Log.d("dmdd22", "11")
                    Log.d("dmdd", "" + title1)
                    tvPlay.visibility = View.INVISIBLE
                    tvMore.visibility = View.INVISIBLE
                    tvTitle.text = title1
                    tvDesc.text = description
                    tvGen.text = genere
                    Glide.with(applicationContext).load(bannerImageUrl)
                        .into(imageBanner)
                    homeFragment!!.loadData(id)
                    homeFragment!!.view!!.requestFocus()
                }
                else if (searchLayout.visibility == View.VISIBLE)
                {
                    Log.d("dmdd22", "22")
                    if (btnHome.hasFocus() || btnSearch.hasFocus() || profile.hasFocus()|| signIn.hasFocus() || setting.hasFocus()) {
                        Log.d("dmdd", "33")
                        searchEditText.requestFocus()
                    }
                    else if (searchEditText.hasFocus() && searchData.equals("yes"))
                    {
                        Log.d("dmdd", "44")
                        searchFragment!!.view!!.requestFocus()
//                        true
//                        super.onKeyDown(keyCode, event)
                    }
                    else if (searchData.equals("no"))
                    {
                        true
                    }

                    else{
//                        true
                        super.onKeyDown(keyCode, event)
                    }
                } else {
                    super.onKeyDown(keyCode, event)
                }
            }

//            KeyEvent.KEYCODE_DPAD_CENTER->
//            {
//                Toast.makeText(applicationContext,"done",Toast.LENGTH_SHORT).show()
//                hideKeyboard()
//                searchEditText.requestFocus()
//                super.onKeyDown(keyCode, event)
//
//            }
            KeyEvent.KEYCODE_BACK -> {
                if (searchLayout.visibility == View.VISIBLE) {
                    Log.d("dm", "11")
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                    hideKeyboard()
                    searchLayout.visibility = View.GONE
                    searchFragment!!.view!!.visibility = View.GONE
                    movieNotFoundText.visibility = View.GONE
                    searchEditText.text!!.clear()
                    searchData = "no"
                    btnSearch.requestFocus()
                } else if (layout.visibility == View.VISIBLE) {
                    Log.d("dm", "44")
                    layout.visibility = View.GONE
                    signIn.requestFocus()
                } else if (layoutHometc.visibility == View.VISIBLE) {
                    Log.d("zz", "data:   " + data)

                    Log.d("dm", "55")
                    if (data.equals("1")) {
                        tcScrollView.clearFocus()
                        textHomeTc.requestFocus()
                        layoutHometc.visibility = View.GONE
//                        tcScrollView.fullScroll(ScrollView.FOCUS_UP)
                    } else {
                        Log.d("dm", "66")
                        tcScrollView.clearFocus()
                        textHomepp.requestFocus()
                        layoutHometc.visibility = View.GONE
                    }
                } else if (layoutSetting.visibility == View.VISIBLE) {
                    Log.d("dm", "77")
                    layoutSetting.visibility = View.GONE
                    setting.requestFocus()
                } else {
                    exitByBackKey()
                }
                return true
            }


            else -> super.onKeyDown(keyCode, event)
        }
    }




    private fun letsLogin(email: String, pass: String) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", pass)
            val requestBody = jsonObject.toString()
            val stringRequest: StringRequest = object :
                StringRequest(Method.POST, Api.baseUrl2 + "/login", Response.Listener { response ->
                    progressBar.visibility = View.VISIBLE
                    try {
                        val jsonObject = JSONObject(response)
                        val loginModel = LoginModel()
                        loginModel.birthYear = jsonObject.getString("birth_year")
                        loginModel.isEpisodeReviewMessageSeen =
                            jsonObject.getBoolean("is_episode_review_message_seen")
                        loginModel.isTutorialPageSeen =
                            jsonObject.getBoolean("is_tutorial_page_seen")
                        loginModel.isTutorialScenePageSeen =
                            jsonObject.getBoolean("is_tutorial_scene_page_seen")
                        loginModel.emailAddress = jsonObject.getString("email_address")
                        loginModel.fName = jsonObject.getString("f_name")
                        loginModel.lName = jsonObject.getString("l_name")
                        loginModel.thumbImage = jsonObject.getString("thumb_image")
                        loginModel.image = jsonObject.getString("image")
                        loginModel.birthMonth = jsonObject.getString("birth_month")
                        loginModel.role = jsonObject.getString("role")
                        loginModel.xAuthToken = jsonObject.getString("x_auth_token")
                        loginModel.userId = jsonObject.getString("user_id")
                        Log.d("zz", "login " + response)
                        val token = jsonObject.getString("x_auth_token")
                        val name = jsonObject.getString("f_name")
                        val image = jsonObject.getString("thumb_image")
                        lateinit var signInPreferences: SharedPreferences
                        signInPreferences = getSharedPreferences("authData", Activity.MODE_PRIVATE)
                        val editor = signInPreferences!!.edit()
                        editor.putString("token", token)
                        editor.putString("name", name)
                        editor.putString("image", image)
                        editor.apply()
//                    layout.visibility = View.GONE
//                    progressBar.visibility = View.GONE
                        this.recreate()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    Log.e("VOLLEY", error.toString())
                    Toast.makeText(
                        applicationContext,
                        "username or password was entered incorrectly",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE

                }) {
                override fun getBodyContentType(): String {
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        requestBody?.toByteArray(charset("utf-8"))
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
                    return headers
                }
            }
            stringRequest.setShouldCache(false)
            applicationContext?.let {
                VolleySingleton.getInstance(it).addToRequestQueue(stringRequest)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    protected fun exitByBackKey() {
        val alertbox = AlertDialog.Builder(this).setTitle("Flmly")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { arg0, arg1 ->
                finish()
            }
            .setNegativeButton(
                "No" // do something when the button is clicked
            ) { arg0, arg1 -> }
            .show()
    }


    fun checkSignUpEdittxt() {
        if (etEmail.getText().toString().trim().equals("") || etEmail.getText()
                .toString().trim() == ""
        ) {
            Log.e("aaa", "    2");
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show()

        } else if (etPass.getText().toString().trim()
                .equals("") || etPass.getText().toString().trim() == "" || etPass.text.toString()
                .trim().length < 6
        ) { //            Log.e(TAG, "    3");
            Toast.makeText(
                applicationContext,
                "Password length should be more then 6 charecters",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (etEmail.getText().toString().trim().matches(emailPattern)) {

            letsLogin(etEmail.getText().toString().trim(), etPass.getText().toString().trim())
        } else {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
        }
    }


    fun loadTc() {
        try {
            progressBar.visibility = View.VISIBLE
            val stringRequest = object : StringRequest(
                Method.GET, "https://www.flmly.com/api/txt/terms_of_service",
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    text_tc = jsonObject.getString("text")
//                    text_pp=jsonObject.getString("privacy")
                    progressBar.visibility = View.GONE

//                    Log.d(TAG,"privacy"+jsonObject.getString("privacy"))
//                    Log.d(TAG,"terms"+jsonObject.getString("terms"))

                }, Response.ErrorListener {
                    Toast.makeText(

                        applicationContext,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
//                    HomeActivity.progressBar.visibility=View.GONE


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


    fun loadPolicy() {
        try {
            progressBar.visibility = View.VISIBLE
            val stringRequest = object : StringRequest(
                Method.GET, "https://www.flmly.com/api/txt/privacy_policy",
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    text_pp = jsonObject.getString("text")
                    progressBar.visibility = View.GONE

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
                    return headers
                }
            }

            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0)
    }





}
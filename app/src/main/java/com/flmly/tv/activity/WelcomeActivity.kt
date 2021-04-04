package com.flmly.tv.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flmly.tv.R
import com.flmly.tv.VolleySingleton
import com.flmly.tv.model.LoginModel
import com.flmly.tv.utility.Api
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.UnsupportedEncodingException

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class WelcomeActivity : Activity() {

    private val TAG: String? = WelcomeActivity::class.java.getSimpleName()
    lateinit var tvLogin:TextView
    lateinit var tvContineu:TextView
    lateinit var webview: WebView
    lateinit var tvcontent:TextView
    lateinit var tvPrivacy:AppCompatTextView
    lateinit var tcScrollView: ScrollView
    lateinit var tvTerms:AppCompatTextView
    lateinit var tvSignp:TextView
    lateinit var tvSignupText:AppCompatTextView
    lateinit var tvsignupPrice:AppCompatTextView
    lateinit var tvTc:TextView
    lateinit var tvskipp:TextView
    lateinit var tvBack:TextView
    var unscribe1:String=""
    var unscribe2:String=""
    private var mStatusCode = 0
    lateinit var tvSubscribe:AppCompatTextView
    lateinit var layout_tc:ConstraintLayout
    lateinit var layout_signUp:ConstraintLayout
    lateinit var layout:ConstraintLayout
    lateinit var etEmail:AppCompatEditText
    lateinit var etPass:AppCompatEditText
    lateinit var progressBar: ProgressBar
    lateinit var signInPreferences: SharedPreferences

    var text_tc: String=""
    var text_pp: String=""
    var data:String=""
    val emailPattern =
        Regex("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

//        webview=findViewById(R.id.webview)
        tvLogin=findViewById(R.id.tvLogin)
        layout=findViewById(R.id.LoginLayout)
        etEmail=findViewById(R.id.etEmail)
        etPass=findViewById(R.id.etPass)
        tvcontent=findViewById(R.id.tvContent)
        tvSignp=findViewById(R.id.tvSignp)
        tvPrivacy=findViewById(R.id.tv_privacy)
        tvTerms=findViewById(R.id.tv_terms)
        tvBack=findViewById(R.id.tvBack)
//        tvTc=findViewById(R.id.tvTc)
        tvsignupPrice=findViewById(R.id.tvsignupPrice)
        tvskipp=findViewById(R.id.tvskipp)
        progressBar=findViewById(R.id.welcomeProgress)
        tvContineu=findViewById(R.id.tvContinue)
        tvSubscribe=findViewById(R.id.tv_subscribe)
        tcScrollView=findViewById(R.id.tcScroll)
        layout_tc=findViewById(R.id.layout_tc)
        layout_signUp=findViewById(R.id.layout_signup)
        tvSignupText=findViewById(R.id.tvSignupText)


        val text = "Sign up on the web at "+"<font color=#9400d3>www.flmly.com</font>"
        tvSignupText.setText(Html.fromHtml(text))

//        tvSignupText.text=
//        loadUnsubscribe()
        loadSubscription()
        loadPolicy()
        loadTc()

        tvContineu.setOnClickListener {
            checkSignUpEdittxt()
        }

        tvLogin.setOnClickListener{
            layout.visibility=View.VISIBLE
            etEmail.requestFocus()

        }




        tvTerms.setOnClickListener {
            data="1"
            tvcontent.setText("")
            tcScrollView.requestFocus()
//            tvTc.setText("Terms and conditions")
            layout_tc.visibility=View.VISIBLE
            var input:String=Jsoup.parse(text_tc).wholeText()
//                        tvcontent.setText(Html.fromHtml(input)).

            tvcontent.text=input

        }
        tvPrivacy.setOnClickListener {
            data="2"
            tvcontent.setText("")
            tcScrollView.requestFocus()
//            tvTc.setText("Privacy Policy")
            layout_tc.visibility=View.VISIBLE
            Log.d("zz","pp"+Html.fromHtml(text_pp))
//            tvcontent.setText(Html.fromHtml(text_pp))
            tvcontent.text= Jsoup.parse(text_pp).wholeText()

        }

        tvPrivacy.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
//                tvPrivacy.setTextSize(14f) //increased size
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvPrivacy.setTypeface(type)
//                tvPrivacy.setTypeface(null, )
            } else {
//                tvPrivacy.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvPrivacy.setTypeface(type)
                tvPrivacy.setTextSize(12f) //normal size
            }
        })

        tvTerms.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val type = Typeface.createFromAsset(assets, "font/myriadpro_bold.otf")
                tvTerms.setTypeface(type)
//                tvTerms.setTextSize(14f) //increased size
//                tvTerms.setTypeface(null, Typeface.BOLD)

//                ((TextView)findViewById(R.id.tv_terms)).setTypeface(Typeface.DEFAULT_BOLD);
//                tvTerms.setTypeface(Typeface.BOLD)
            } else {
//                tvTerms.setTypeface(null, Typeface.NORMAL)
                val type = Typeface.createFromAsset(assets, "font/myriad_pro_regular.ttf")
                tvTerms.setTypeface(type)
                tvTerms.setTextSize(12f) //normal size

            }
        })

        tvSignp.setOnClickListener {
            layout_signUp.visibility=View.VISIBLE
            tvBack.requestFocus()

        }
        tvBack.setOnClickListener {
            layout_signUp.visibility=View.GONE
            tvSignp.requestFocus()
        }

        tvskipp.setOnClickListener {

            val intent= Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    override fun onResume() {
        super.onResume()
        signInPreferences = getSharedPreferences("authData", MODE_PRIVATE)
        tvSignp.requestFocus()
    }

    fun checkSignUpEdittxt() {
        if (etEmail.getText().toString().trim().equals("") || etEmail.getText()
                .toString().trim() == "") {
            Log.e("aaa", "    2");
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show()

        } else if (etPass.getText().toString().trim()
                .equals("") || etPass.getText().toString().trim() == "" || etPass.text.toString().trim().length < 6
        ) { //            Log.e(TAG, "    3");
            Toast.makeText(
                applicationContext,
                "Password length should be more then 6 charecters",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (etEmail.getText().toString().trim().matches(emailPattern))
        {

            letsLogin(etEmail.getText().toString().trim(),etPass.getText().toString().trim())
        } else {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadTc() {
        try {
            progressBar.visibility=View.VISIBLE
            val stringRequest = object : StringRequest(
                Method.GET, "https://www.flmly.com/api/txt/terms_of_service",
                Response.Listener { response ->
                    Log.e(TAG, "$response")
                    val jsonObject = JSONObject(response)
                    text_tc=jsonObject.getString("text")
//                    text_pp=jsonObject.getString("privacy")
                    progressBar.visibility=View.GONE

//                    Log.d(TAG,"privacy"+jsonObject.getString("privacy"))
//                    Log.d(TAG,"terms"+jsonObject.getString("terms"))

                }, Response.ErrorListener {
                    Toast.makeText(

                        applicationContext,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility=View.GONE
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
        }catch (e: JSONException) {
            e.printStackTrace()
        }
    }



    fun loadPolicy() {
        try {
            progressBar.visibility=View.VISIBLE
            val stringRequest = object : StringRequest(
                Method.GET, "https://www.flmly.com/api/txt/privacy_policy",
                Response.Listener { response ->
                    Log.e(TAG, "$response")
                    val jsonObject = JSONObject(response)
                    text_pp=jsonObject.getString("text")
                    progressBar.visibility=View.GONE

                }, Response.ErrorListener {
                    Toast.makeText(

                        applicationContext,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility=View.GONE
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
        }catch (e: JSONException) {
            e.printStackTrace()
        }
    }




    fun loadSubscription() {
        try {
            progressBar.visibility=View.VISIBLE
            val stringRequest = object : StringRequest(
                Method.GET, Api.baseUrl + "settings/get_settings?from=tv&flmly_yearly_subscription_amount=1&flmly_monthly_subscription_amount=1",
                Response.Listener { response ->
                    Log.e(TAG, "$response")
                    val jsonObject = JSONObject(response)
                    val js2=jsonObject.getJSONObject("data")
                    tvSubscribe.text="Subscribe for $"+js2.getString("flmly_monthly_subscription_amount")+" /month"
                    tvsignupPrice.text="Unlimited viewing for $"+js2.getString("flmly_monthly_subscription_amount")+" /month"
                    progressBar.visibility=View.GONE
//                    text_pp=jsonObject.getString("privacy")

//                    Log.d(TAG,"privacy"+jsonObject.getString("privacy"))
//                    Log.d(TAG,"terms"+jsonObject.getString("terms"))

                }, Response.ErrorListener {
                    Toast.makeText(
                        applicationContext,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility=View.GONE
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
        }catch (e: JSONException) {
            e.printStackTrace()
        }
    }


//    fun loadUnsubscribe() {
//        try {
//            progressBar.visibility=View.VISIBLE
//            val stringRequest = object : StringRequest(
//                Method.GET, Api.baseUrl + "settings/get_settings?from=tv&unsubscribe_user_film_watch_count=1&unsubscribe_user_film_watch_time=1",
//                Response.Listener { response ->
//                    Log.e(TAG, "$response")
//                    val jsonObject = JSONObject(response)
//                    val js2=jsonObject.getJSONObject("data")
//                    unscribe1=js2.getString("unsubscribe_user_film_watch_count")
//                    unscribe2=js2.getString("unsubscribe_user_film_watch_time")
//                    progressBar.visibility=View.GONE
////                    text_pp=jsonObject.getString("privacy")
//
////                    Log.d(TAG,"privacy"+jsonObject.getString("privacy"))
////                    Log.d(TAG,"terms"+jsonObject.getString("terms"))
//
//                }, Response.ErrorListener {
//                    Toast.makeText(
//                        applicationContext,
//                        "Something went wrong.",
//                        Toast.LENGTH_SHORT
//                    ).show()
////                    HomeActivity.progressBar.visibility=View.GONE
//                    progressBar.visibility=View.GONE
//
//
//                }) {
//                override fun getHeaders(): Map<String, String> {
//                    val headers = HashMap<String, String>()
//                    headers["Accept"] = "application/json"
//                    headers["api-key"] = "6vQKnb_G5c6"
//                    return headers
//                }
//            }
//
//            val requestQueue = Volley.newRequestQueue(applicationContext)
//            requestQueue.add(stringRequest)
//        }catch (e: JSONException) {
//            e.printStackTrace()
//        }
//    }


    override fun onBackPressed() {
        if (layout_tc.visibility==View.VISIBLE)
        {
            layout_tc.visibility=View.GONE
            tcScrollView.fullScroll(ScrollView.FOCUS_UP)
            tvcontent.setText("")
            if (data.equals("1")) {
                tvTerms.requestFocus()
            }
            else{
                tvPrivacy.requestFocus()
            }
        }
        else if (layout.visibility==View.VISIBLE)
        {
            layout.visibility=View.GONE
        }
        else if (layout_signUp.visibility==View.VISIBLE)
        {
            layout_signUp.visibility=View.GONE
            tvSignp.requestFocus()
        }
        else {
            super.onBackPressed()
        }
    }


    private fun letsLogin(email:String, pass:String) {
        progressBar.visibility=View.VISIBLE
        val jsonObject1 = JSONObject()
        jsonObject1.put("email", email)
        jsonObject1.put("password", pass)
        val requestBody = jsonObject1.toString()
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Api.baseUrl2+"/login", Response.Listener { response ->
            Log.d("statusCode", mStatusCode.toString())
            try {
                val jsonObject = JSONObject(response)
                val loginModel = LoginModel()
                loginModel.birthYear = jsonObject.getString("birth_year")
                loginModel.isEpisodeReviewMessageSeen = jsonObject.getBoolean("is_episode_review_message_seen")
                loginModel.isTutorialPageSeen = jsonObject.getBoolean("is_tutorial_page_seen")
                loginModel.isTutorialScenePageSeen = jsonObject.getBoolean("is_tutorial_scene_page_seen")
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
                val editor = signInPreferences!!.edit()
                editor.putString("token", token)
                editor.putString("name", name)
                editor.putString("image", image)
                editor.apply()
                val intent = Intent(this,
                    HomeActivity::class.java)
                startActivity(intent)
                finish()
                progressBar.visibility = View.GONE
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error -> Log.e("VOLLEY", error.toString())
            Log.d("status  ",""+mStatusCode)
            Toast.makeText(applicationContext,"username or password was entered incorrectly",Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE

        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                if (response != null) {
                    mStatusCode = response.statusCode
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
                return headers
            }
        }
        stringRequest.setShouldCache(false)
        applicationContext?.let {
            VolleySingleton.getInstance(it).addToRequestQueue(stringRequest)
        }


//        } catch (e: JSONException) {
//            e.printStackTrace()
//
//        }
    }

}
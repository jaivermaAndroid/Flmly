package com.flmly.tv.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.flmly.tv.R

class SplashActivity : Activity() {
    lateinit var sharedPreferences: SharedPreferences
    var auth:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        sharedPreferences = getSharedPreferences("authData", MODE_PRIVATE)
        auth = sharedPreferences.getString("token", "")!!
        Log.d("cook","   "+auth)


        val connectivityManager = applicationContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork?.isConnected!=null) {
            if (auth.isNullOrEmpty() ) {
                Handler().postDelayed({
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                }, 2000)
            } else {
                Handler().postDelayed({
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }, 2000)
            }
        }
        else{
            Toast.makeText(
                    applicationContext,
                    "Check your internet connection",
                    Toast.LENGTH_SHORT
            ).show()
        }







    }
}
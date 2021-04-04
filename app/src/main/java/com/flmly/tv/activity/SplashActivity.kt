package com.flmly.tv.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
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
}
package com.capstone.nutricipe.ui.activity.intro

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.capstone.nutricipe.R
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.utils.SharedPreference

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val sharedPref: SharedPreference by lazy {
        SharedPreference(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if (sharedPref.getBoolean(PREF_IS_STARTED) == true){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }else {
                startActivity(Intent(applicationContext, OnboardingActivity::class.java))
                finish()
            }
        }, 3000)
    }

    companion object{
        const val PREF_IS_STARTED = "PREF_IS_STARTED"
    }
}
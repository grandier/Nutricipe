package com.capstone.nutricipe.ui.activity.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.nutricipe.databinding.ActivitySplashBinding
import com.capstone.nutricipe.ui.activity.MainActivity
import com.capstone.nutricipe.ui.utils.SharedPreference

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val sharedPref: SharedPreference by lazy {
        SharedPreference(this)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        Handler().postDelayed({
            if (sharedPref.getBoolean(PREF_IS_STARTED)){
                startActivity(Intent(applicationContext, MainActivity::class.java))
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
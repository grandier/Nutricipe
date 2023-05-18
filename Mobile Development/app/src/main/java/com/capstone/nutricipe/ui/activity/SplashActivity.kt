package com.capstone.nutricipe.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.nutricipe.databinding.ActivitySplashBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Uji Coba Doang
        binding.btnStart.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
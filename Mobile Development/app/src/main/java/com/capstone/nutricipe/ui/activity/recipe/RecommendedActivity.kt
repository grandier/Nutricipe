package com.capstone.nutricipe.ui.activity.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.nutricipe.R
import com.capstone.nutricipe.databinding.ActivityRecommendedBinding
import com.capstone.nutricipe.databinding.ActivitySplashBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity

class RecommendedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Uji Coba Doang
        binding.btnBack.setOnClickListener{
            finish()
        }
    }
}
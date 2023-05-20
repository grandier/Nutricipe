package com.capstone.nutricipe.ui.activity.recipe

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.databinding.ActivityProfileBinding
import com.capstone.nutricipe.databinding.ActivityRecommendedBinding
import com.capstone.nutricipe.databinding.ActivitySplashBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.activity.dataStore
import com.capstone.nutricipe.ui.viewmodel.ProfileViewModel
import com.capstone.nutricipe.ui.viewmodel.RecommendedViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory

class RecommendedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendedBinding
    private lateinit var recommendedViewModel: RecommendedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idHistory = intent.getStringExtra("idHistory")

        // Uji Coba Doang
        binding.btnBack.setOnClickListener {
            finish()
        }

        val pref = Session.getInstance(dataStore)

        recommendedViewModel = ViewModelProvider(
            this, ViewModelFactory(pref, this)
        )[RecommendedViewModel::class.java]

        recommendedViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        recommendedViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                if (!idHistory.isNullOrEmpty()) {
                    recommendedViewModel.getUploaded(token, idHistory)
                } else {
                    // Handle the case where idHistory is not available
                    Toast.makeText(this, "idHistory is not available", Toast.LENGTH_SHORT).show()
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        recommendedViewModel.uploaded.observe(this) { uploaded ->
            if (uploaded != null) {
                binding.tvTitle.text = uploaded.title
                binding.tvDescription.text = uploaded.description

                Glide.with(this)
                    .load(uploaded.imageUrl)
                    .into(binding.previewImageView)
            }
        }

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}


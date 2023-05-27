package com.capstone.nutricipe.ui.activity.recipe

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import coil.Coil
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.remote.model.ResultItem
import com.capstone.nutricipe.databinding.ActivityProfileBinding
import com.capstone.nutricipe.databinding.ActivityRecommendedBinding
import com.capstone.nutricipe.databinding.ActivitySplashBinding
import com.capstone.nutricipe.ui.activity.MainActivity
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
        val recommendedHistory = intent.getParcelableExtra<ResultItem>("Photo")


        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
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
                } else if (recommendedHistory != null) {
                    binding.tvTitle.text = recommendedHistory.title
                    binding.tvDescription.text = recommendedHistory.description
                    Glide.with(this)
                        .load(recommendedHistory.imageUrl)
                        .into(binding.previewImageView)
                } else {
                    // Handle the case where both idHistory and recommendedHistory are not available
                    Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        recommendedViewModel.uploaded.observe(this) { uploaded ->
            if (uploaded != null) {
                bindRecommendedData(uploaded)
            }
        }

        binding.ivSetting.setOnClickListener {
            showPopupMenu(it, recommendedHistory?.id ?: idHistory ?: "")
        }

        recommendedViewModel.messageDeleted.observe(this) { messageDeleted ->
            if(messageDeleted == "Delete Success"){
                Toast.makeText(this, messageDeleted, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
            else {
                Toast.makeText(this, messageDeleted, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindRecommendedData(recommendedData: ResultItem) {
        binding.tvTitle.text = recommendedData.title
        binding.tvDescription.text = recommendedData.description

        showLoading(true) // Set isLoading to true before starting image loading

        val request = ImageRequest.Builder(this)
            .data(recommendedData.imageUrl)
            .target { drawable ->
                binding.previewImageView.setImageDrawable(drawable)
                showLoading(false) // Set isLoading to false when image loading is completed
            }
            .build()

        Coil.imageLoader(this).enqueue(request)
    }

    private fun showPopupMenu(view: View, id: String) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.popup_menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_delete -> {
                    Log.e("yang masuk adalah id:", id)
                    // Handle delete action
                    deleteHistory(id)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun deleteHistory(idHistory: String) {
        Log.e("history", idHistory)
        recommendedViewModel.getToken().observe(this) { token ->
            if (!token.isNullOrBlank()) {
                recommendedViewModel.deleteHistory(token, idHistory)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }
}


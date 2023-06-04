package com.capstone.nutricipe.ui.activity.recipe

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.paging.adapter.RecipeAdapter
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.remote.model.DataRecipeItem
import com.capstone.nutricipe.data.remote.model.RecipeItem
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
        Log.e("recommendedHistory", recommendedHistory.toString())


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
                    recommendedViewModel.getUploadedRecipe(token, idHistory)
                } else if (recommendedHistory != null) {
                    recommendedViewModel.getUploadedRecipe(token, recommendedHistory.id)
                    binding.tvTitle.text = recommendedHistory.title
                    binding.tvDescription.text = recommendedHistory.description
                    Glide.with(this)
                        .load(recommendedHistory.imageUrl)
                        .into(binding.ivPreview)
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

        recommendedViewModel.listRecipe.observe(this) { listRecipe ->
            showRecipes(listRecipe)
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

    private fun bindRecommendedData(recommendedData: DataRecipeItem) {
        binding.tvTitle.text = recommendedData.title
        binding.tvDescription.text = recommendedData.description

        showLoading(true) // Set isLoading to true before starting image loading

        val request = ImageRequest.Builder(this)
            .data(recommendedData.imageUrl)
            .target { drawable ->
                binding.ivPreview.setImageDrawable(drawable)
                showLoading(false) // Set isLoading to false when image loading is completed
            }
            .build()

        Coil.imageLoader(this).enqueue(request)
    }

    private fun showPopupMenu(view: View, id: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete Confirmation")
        alertDialogBuilder.setMessage("Are you sure you want to delete?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            deleteHistory(id)
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setOnShowListener {
            val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.red))

            val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        alertDialog.show()
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

    private fun showRecipes(recipes: List<RecipeItem>) {
        binding.rvRecipe.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@RecommendedActivity, RecyclerView.HORIZONTAL, false)
            adapter = RecipeAdapter(ArrayList(recipes))
        }
    }
}


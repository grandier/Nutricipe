package com.capstone.nutricipe.ui.activity.recipe

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window

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
import com.capstone.nutricipe.databinding.CardDialogBinding
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

        binding.ivDelete.setOnClickListener {
            deleteDialog(recommendedHistory?.id ?: idHistory ?: "")
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

        showLoading(true)

        val request = ImageRequest.Builder(this)
            .data(recommendedData.imageUrl)
            .target { drawable ->
                binding.ivPreview.background = ColorDrawable(Color.TRANSPARENT)
                binding.ivPreview.setImageDrawable(drawable)
                showLoading(false)
            }
            .build()

        Coil.imageLoader(this).enqueue(request)
    }

    private fun deleteDialog(id: String) {
        val dialogBinding = CardDialogBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root

        dialogBinding.tvTitleDialog.text = getString(R.string.title_delete)
        dialogBinding.tvTextDialog.text = getString(R.string.text_delete)
        dialogBinding.btnNo.text = getString(R.string.no)
        dialogBinding.btnYes.text = getString(R.string.yes)

        val dialogBuilder = Dialog(this)
        dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setContentView(dialogView)
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBuilder.show()

        dialogBinding.btnYes.setOnClickListener {
            deleteHistory(id)
        }
        dialogBinding.btnNo.setOnClickListener {
            dialogBuilder.dismiss()
        }
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
            binding.loadingShimmer.visibility = View.VISIBLE
        } else {
            binding.loadingShimmer.visibility = View.GONE
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


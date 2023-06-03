package com.capstone.nutricipe.ui.activity.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.remote.model.RecipeItem
import com.capstone.nutricipe.databinding.ActivityDetailBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.activity.dataStore
import com.capstone.nutricipe.ui.viewmodel.DetailViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipe = intent.getParcelableExtra<RecipeItem>("Recipe")

        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val pref = Session.getInstance(dataStore)
        detailViewModel = ViewModelProvider(
            this, ViewModelFactory(pref, this)
        )[DetailViewModel::class.java]

        detailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        detailViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                if (recipe != null) {
                    binding.tvTitle.text = recipe.title
                    Glide.with(this)
                        .load(recipe.image)
                        .into(binding.previewImageView)

                    val usedIngredients = "<b>Used Ingredients:</b> ${recipe.usedIngredients}<br><br>"
                    val missedIngredients = "<b>Missed Ingredients:</b> ${recipe.missedIngredients}<br><br>"
                    val amounts = recipe.amount.joinToString("") { "<li> ${it}</li>" }
                    val amount = "<b>Amount:</b><br><ul>$amounts</ul><br>"
                    val instruction = "<b>Instruction:</b> ${recipe.instruction}"

                    val combinedIngredients = "$usedIngredients$missedIngredients$amount$instruction"

                    binding.viewTextRecipeDetail.text = HtmlCompat.fromHtml(combinedIngredients, HtmlCompat.FROM_HTML_MODE_LEGACY)

                }
                else {
                    // Handle the case where both idHistory and recommendedHistory are not available
                    Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
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
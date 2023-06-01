package com.capstone.nutricipe.data.paging.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.remote.model.RecipeItem
import com.capstone.nutricipe.data.remote.model.ResultItem
import com.capstone.nutricipe.databinding.CardHistoryBinding
import com.capstone.nutricipe.ui.activity.MainActivity
import com.capstone.nutricipe.ui.activity.recipe.DetailActivity
import com.capstone.nutricipe.ui.activity.recipe.RecommendedActivity

class RecipeAdapter(private val listRecipes: ArrayList<RecipeItem>) :
    RecyclerView.Adapter<RecipeAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listRecipes[position])
    }

    override fun getItemCount(): Int = listRecipes.size

    inner class ListViewHolder(private val binding: CardHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val photoView: ImageView = binding.storyImage
        private val name: TextView = binding.storyTitle
        fun bind(recipe: RecipeItem) {
            name.text = recipe.title
            Glide.with(itemView.context).load(recipe.image).into(photoView)


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("Recipe", recipe)

                val options = ActivityOptionsCompat.makeCustomAnimation(
                    itemView.context,
                    R.anim.slide_in_right, // Enter animation (slide from right)
                    R.anim.slide_out_left // Exit animation (slide to left)
                ).toBundle()

                itemView.context.startActivity(intent, options)
            }
        }
    }

}
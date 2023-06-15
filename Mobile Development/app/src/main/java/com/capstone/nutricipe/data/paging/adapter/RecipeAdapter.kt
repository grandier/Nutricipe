package com.capstone.nutricipe.data.paging.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.remote.model.RecipeItem
import com.capstone.nutricipe.databinding.CardRecipeBinding
import com.capstone.nutricipe.ui.activity.recipe.DetailActivity

class RecipeAdapter(private val listRecipes: ArrayList<RecipeItem>) :
    RecyclerView.Adapter<RecipeAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listRecipes[position])
    }

    override fun getItemCount(): Int = listRecipes.size

    inner class ListViewHolder(binding: CardRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val photoView: ImageView = binding.ivRecipe
        private val name: TextView = binding.tvRecipe
        fun bind(recipe: RecipeItem) {
            name.text = recipe.title
            Glide.with(itemView.context).load(recipe.image).into(photoView)


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("Recipe", recipe)

                val options = ActivityOptionsCompat.makeCustomAnimation(
                    itemView.context,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ).toBundle()

                itemView.context.startActivity(intent, options)
            }
        }
    }

}
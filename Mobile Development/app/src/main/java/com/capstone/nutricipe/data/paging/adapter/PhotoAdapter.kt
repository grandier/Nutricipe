package com.capstone.nutricipe.data.paging.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.remote.model.ResultItem
import com.capstone.nutricipe.databinding.CardHistoryBinding
import com.capstone.nutricipe.ui.activity.profile.ProfileActivity
import com.capstone.nutricipe.ui.activity.recipe.RecommendedActivity

class PhotoAdapter :
    PagingDataAdapter<ResultItem, PhotoAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ListViewHolder(private val binding: CardHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val photoView: ImageView = binding.storyImage
        private val name: TextView = binding.storyTitle

        fun bind(photo: ResultItem) {
            name.text = photo.title
            Glide.with(itemView.context).load(photo.imageUrl).into(photoView)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, RecommendedActivity::class.java)
                intent.putExtra("Photo", photo)

                val options = ActivityOptionsCompat.makeCustomAnimation(
                    itemView.context,
                    R.anim.slide_in_right, // Enter animation (slide from right)
                    R.anim.slide_out_left // Exit animation (slide to left)
                ).toBundle()

                itemView.context.startActivity(intent, options)
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultItem>() {
            override fun areItemsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ResultItem,
                newItem: ResultItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
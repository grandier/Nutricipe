package com.capstone.nutricipe.data.paging.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.remote.model.ResultItem
import com.capstone.nutricipe.databinding.CardHistoryBinding
import com.capstone.nutricipe.ui.activity.recipe.RecommendedActivity

class HistoryAdapter :
    PagingDataAdapter<ResultItem, HistoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

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

    inner class ListViewHolder(binding: CardHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val photoView: ImageView = binding.ivHistory
        private val name: TextView = binding.tvHistory

        fun bind(photo: ResultItem) {
            name.text = photo.title
            Glide.with(itemView.context).load(photo.imageUrl).into(photoView)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, RecommendedActivity::class.java)
                intent.putExtra("Photo", photo)

                val options = ActivityOptionsCompat.makeCustomAnimation(
                    itemView.context,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
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
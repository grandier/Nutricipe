package com.capstone.nutricipe.data.remote.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Parcelize
data class UploadedHistory(

	@field:SerializedName("result")
	val result: List<ResultItem>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
@Entity(tableName = "result")
data class ResultItem(

	@field:SerializedName("owner")
	val owner: String,

	@field:SerializedName("createdAt")
	val createdAt: Long,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String
) : Parcelable

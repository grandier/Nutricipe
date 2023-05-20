package com.capstone.nutricipe.data.remote.model

import com.google.gson.annotations.SerializedName

data class Recipe(

	@field:SerializedName("recipe1")
	val recipe1: String? = null
)

data class Temp(

	@field:SerializedName("owner")
	val owner: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("recipe")
	val recipe: Recipe? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class GetUploaded(

	@field:SerializedName("temp")
	val temp: Temp? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

package com.capstone.nutricipe.data.remote.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Parcelize
data class UploadedRecipe(

	@field:SerializedName("dataRecipe")
	val dataRecipe: List<DataRecipeItem>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
@Entity(tableName = "recipe")
data class RecipeItem(

	@field:SerializedName("owner")
	val owner: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("usedIngredients")
	val usedIngredients: String,

	@field:SerializedName("missedIngredients")
	val missedIngredients: String,

	@field:SerializedName("idHistory")
	val idHistory: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String
) : Parcelable

@Parcelize
data class DataRecipeItem(

	@field:SerializedName("owner")
	val owner: String,

	@field:SerializedName("createdAt")
	val createdAt: Long,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("recipe")
	val recipe: List<RecipeItem>,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String
) : Parcelable

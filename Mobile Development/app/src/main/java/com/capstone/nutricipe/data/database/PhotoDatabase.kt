package com.capstone.nutricipe.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.capstone.nutricipe.data.remote.model.Recipe
import com.capstone.nutricipe.data.remote.model.ResultItem
import com.google.gson.Gson

@Database(entities = [ResultItem::class], version = 1, exportSchema = false)
@TypeConverters(PhotoDatabase.Converters::class)
abstract class PhotoDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: PhotoDatabase? = null

        fun getDatabase(context: Context): PhotoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDatabase::class.java, "result_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    class Converters {
        @TypeConverter
        fun fromRecipe(recipe: Recipe): String {
            return Gson().toJson(recipe)
        }

        @TypeConverter
        fun toRecipe(json: String): Recipe {
            return Gson().fromJson(json, Recipe::class.java)
        }
    }
}
package com.capstone.nutricipe.data.di

import android.content.Context
import com.capstone.nutricipe.data.paging.photo.PhotoRepository
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.database.PhotoDatabase

object Injection {
    fun provideRepository(context: Context): PhotoRepository {
        val database = PhotoDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return PhotoRepository(database, apiService)
    }
}
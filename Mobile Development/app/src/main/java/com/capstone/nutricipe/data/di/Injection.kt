package com.capstone.nutricipe.data.di

import android.content.Context
import com.capstone.nutricipe.data.paging.photo.PhotoRepository
import com.capstone.nutricipe.data.remote.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): PhotoRepository {
        val apiService = ApiConfig.getApiService()
        return PhotoRepository(apiService)
    }
}
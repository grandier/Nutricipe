package com.capstone.nutricipe.data.remote.api

import de.hdodenhof.circleimageview.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    const val BASE_URL = "http://10.0.2.2:5000/nutricipe/"

    fun getApiService(): ApiService {
        val client = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    } else {
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                    }

                }
                addInterceptor(loggingInterceptor)
            }
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)

    }
}
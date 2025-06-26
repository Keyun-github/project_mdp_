package com.example.projectmdp.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val json = Json { ignoreUnknownKeys = true }
    //kelun
//    private const val APP_BASE_URL = "http://192.168.101.86:3000/"
    //meme
//    private const val APP_BASE_URL = "http://192.168.100.236:3000/"
    //memekmps
    private const val APP_BASE_URL = "http://192.168.101.74:3000/"



    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(APP_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    private const val HF_BASE_URL = "https://api-inference.huggingface.co/"

    val hfInstance: HuggingFaceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(HF_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(HuggingFaceApiService::class.java)
    }
}
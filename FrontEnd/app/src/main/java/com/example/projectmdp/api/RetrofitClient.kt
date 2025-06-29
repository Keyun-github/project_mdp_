package com.example.projectmdp.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit // <-- Impor yang dibutuhkan untuk TimeUnit

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // Waktu untuk membuat koneksi
        .readTimeout(30, TimeUnit.SECONDS)    // Waktu untuk membaca data dari server
        .writeTimeout(30, TimeUnit.SECONDS)   // Waktu untuk mengirim data ke server
        .build()
    // ==========================================================


    private val json = Json { ignoreUnknownKeys = true }

//    private const val APP_BASE_URL = "http://192.168.1.67:3000/"
//    ip meme
private const val APP_BASE_URL = "http://192.168.100.236:3000/"


    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(APP_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    private const val COHERE_BASE_URL = "https://api.cohere.ai/"

    val cohereInstance: CohereApiService by lazy {
        Retrofit.Builder()
            .baseUrl(COHERE_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(CohereApiService::class.java)
    }
}
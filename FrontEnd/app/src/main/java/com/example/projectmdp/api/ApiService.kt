package com.example.projectmdp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @POST("api/auth/register")
    suspend fun registerUser(
        @Body requestBody: RegisterRequest
    ): Response<GenericResponse>

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body requestBody: LoginRequest
    ): Response<LoginResponse>

    @POST("api/donations")
    suspend fun createDonation(
        @Header("Authorization") token: String,
        @Body requestBody: CreateDonationRequest
    ): Response<Donation>

    @GET("api/donations")
    suspend fun getAllDonations(): Response<List<Donation>>

}
package com.example.projectmdp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
    suspend fun createDonation(@Header("Authorization") token: String, @Body requestBody: CreateDonationRequest): Response<Donation>

    @GET("api/donations")
    suspend fun getAllDonations(): Response<List<Donation>>

    @GET("api/donations/{id}")
    suspend fun getDonationById(@Path("id") id: String): Response<Donation>

    @POST("api/donations/{id}/donate")
    suspend fun makeDonation(@Path("id") id: String, @Header("Authorization") token: String, @Body request: MakeDonationRequest): Response<Donation>

    @PUT("api/donations/{id}/stop")
    suspend fun stopDonation(@Path("id") id: String, @Header("Authorization") token: String): Response<GenericResponse>

    @PUT("api/auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body requestBody: UpdateProfileRequest
    ): Response<LoginResponse>
}
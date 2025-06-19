package com.example.projectmdp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    suspend fun registerUser(
        @Body requestBody: RegisterRequest
    ): Response<GenericResponse>

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body requestBody: LoginRequest
    ): Response<LoginResponse>

}
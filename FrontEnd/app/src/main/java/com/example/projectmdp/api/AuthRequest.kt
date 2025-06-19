package com.example.projectmdp.api

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val namaLengkap: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
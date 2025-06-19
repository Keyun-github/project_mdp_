package com.example.projectmdp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenericResponse(
    val message: String
)

@Serializable
data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserData
)

@Serializable
data class UserData(
    val id: String,
    val namaLengkap: String,
    val email: String,
    val role: String,
    @SerialName("customId") val customId: String? = null
)
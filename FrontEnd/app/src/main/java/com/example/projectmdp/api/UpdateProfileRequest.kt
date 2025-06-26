package com.example.projectmdp.api

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val namaLengkap: String?,
    val oldPassword: String?,
    val newPassword: String?
)
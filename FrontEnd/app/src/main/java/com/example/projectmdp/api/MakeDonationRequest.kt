package com.example.projectmdp.api
import kotlinx.serialization.Serializable

@Serializable
data class MakeDonationRequest(
    val amount: Long,
    val donatorName: String
)
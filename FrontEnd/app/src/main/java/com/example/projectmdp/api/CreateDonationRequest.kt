package com.example.projectmdp.api

import kotlinx.serialization.Serializable

@Serializable
data class CreateDonationRequest(
    val title: String,
    val targetAmount: Long,
    val creatorName: String
)
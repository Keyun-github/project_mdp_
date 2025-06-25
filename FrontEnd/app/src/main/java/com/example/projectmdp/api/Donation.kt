package com.example.projectmdp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Donation(
    @SerialName("_id") val id: String,
    val title: String,
    val targetAmount: Long,
    val currentAmount: Long,
    val creatorName: String,
    val creatorId: String,
    val createdAt: String
)

@Serializable
data class CreateDonationRequest(
    val title: String,
    val targetAmount: Long,
    val creatorName: String
)
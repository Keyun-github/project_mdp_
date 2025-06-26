package com.example.projectmdp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class yang merepresentasikan satu entitas Donasi.
 * Strukturnya harus cocok persis dengan JSON yang dikirim oleh server.
 */
@Serializable
data class Donation(
    // Menggunakan @SerialName untuk memetakan field "_id" dari JSON
    // ke properti "id" di Kotlin.
    @SerialName("_id") val id: String,

    val title: String,
    val targetAmount: Long,
    val currentAmount: Long,
    val creatorName: String,
    val creatorId: String,

    // ==========================================================
    // =         PROPERTI YANG DIPERBAIKI (DITAMBAHKAN)         =
    // ==========================================================
    val isActive: Boolean,

    val createdAt: String
)
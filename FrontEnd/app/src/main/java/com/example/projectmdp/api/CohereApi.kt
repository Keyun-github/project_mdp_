package com.example.projectmdp.api

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Model Data untuk Request ke Cohere
@Serializable
data class CohereRequest(
    val message: String
)

// Model Data untuk setiap pesan di dalam chat history
@Serializable
data class ChatHistoryMessage(
    val role: String,
    val message: String
)

// Model Data untuk Response dari Cohere.
@Serializable
data class CohereResponse(
    val text: String,
    val chat_history: List<ChatHistoryMessage>? = null,
    val generation_id: String? = null
)

// API Service Interface untuk Cohere
interface CohereApiService {
    @POST("v1/chat")
    suspend fun getChatResponse(
        @Header("Authorization") token: String,
        @Body request: CohereRequest
    ): Response<CohereResponse>
}
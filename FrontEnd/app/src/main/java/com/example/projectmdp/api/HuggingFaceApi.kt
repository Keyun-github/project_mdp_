package com.example.projectmdp.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Model Data untuk Request
@Serializable
data class HfRequest(val inputs: String)

// Model Data untuk Response
@Serializable(with = HfResponseSerializer::class)
data class HfResponse(
    val generated_text: String
)

// API Service Interface untuk Hugging Face
interface HuggingFaceApiService {
    @POST("models/microsoft/DialoGPT-medium")
    suspend fun getChatResponse(
        @Header("Authorization") token: String,
        @Body request: HfRequest
    ): Response<List<HfResponse>>
}


object HfResponseSerializer : KSerializer<HfResponse> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("HfResponse", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: HfResponse) {
        encoder.encodeString(value.generated_text)
    }

    override fun deserialize(decoder: Decoder): HfResponse {
        val jsonInput = decoder as JsonDecoder
        val jsonObject = jsonInput.decodeJsonElement().jsonObject

        val generatedText = jsonObject["generated_text"]?.jsonPrimitive?.content ?: ""

        return HfResponse(generated_text = generatedText)
    }
}
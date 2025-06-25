package com.example.projectmdp.ai

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmdp.BuildConfig
import com.example.projectmdp.api.HfRequest
import com.example.projectmdp.api.RetrofitClient
import kotlinx.coroutines.launch

sealed class AiResult {
    data class Success(val message: String) : AiResult()
    data class Error(val message: String) : AiResult()
    object Loading : AiResult()
}

class AIChatViewModel : ViewModel() {

    private val _chatResponse = MutableLiveData<AiResult>()
    val chatResponse: LiveData<AiResult> = _chatResponse

    private val apiService = RetrofitClient.hfInstance
    private val authToken = "Bearer ${BuildConfig.HUGGING_FACE_TOKEN}"

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            _chatResponse.value = AiResult.Loading
            try {
                val response = apiService.getChatResponse(authToken, HfRequest(prompt))
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    if (responseBody.isNotEmpty()) {
                        // Respons dari API ini adalah teks lengkap termasuk prompt awal,
                        // jadi kita perlu membersihkannya.
                        val fullText = responseBody[0].generated_text
                        val aiResponse = fullText.replace(prompt, "").trim()
                        _chatResponse.value = AiResult.Success(aiResponse)
                    } else {
                        _chatResponse.value = AiResult.Error("Menerima respons kosong dari AI.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("HF_API_ERROR", "Code: ${response.code()}, Body: $errorBody")
                    _chatResponse.value = AiResult.Error("Gagal mendapatkan respons: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HF_API_ERROR", "Exception: ${e.message}", e)
                _chatResponse.value = AiResult.Error(e.message ?: "Terjadi kesalahan tidak diketahui.")
            }
        }
    }
}
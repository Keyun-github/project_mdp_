package com.example.projectmdp.ai

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmdp.BuildConfig
import com.example.projectmdp.api.CohereRequest
import com.example.projectmdp.api.RetrofitClient
import kotlinx.coroutines.launch

// Sealed class ini tetap sama, tidak perlu diubah
sealed class AiResult {
    data class Success(val message: String) : AiResult()
    data class Error(val message: String) : AiResult()
    object Loading : AiResult()
}

class AIChatViewModel : ViewModel() {

    private val _chatResponse = MutableLiveData<AiResult>()
    val chatResponse: LiveData<AiResult> = _chatResponse

    // Gunakan instance Retrofit yang baru untuk Cohere
    private val apiService = RetrofitClient.cohereInstance

    // Siapkan token otentikasi dari BuildConfig
    private val authToken = "Bearer ${BuildConfig.COHERE_API_KEY}"

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            _chatResponse.value = AiResult.Loading
            try {
                // Buat request body sesuai model data Cohere
                val request = CohereRequest(message = prompt)

                // Panggil API Cohere
                val response = apiService.getChatResponse(authToken, request)

                if (response.isSuccessful && response.body() != null) {
                    // Ambil properti 'text' dari respons yang berhasil
                    _chatResponse.value = AiResult.Success(response.body()!!.text)
                } else {
                    // Jika gagal, catat error lengkapnya untuk debugging
                    val errorBody = response.errorBody()?.string()
                    Log.e("COHERE_API_ERROR", "Code: ${response.code()}, Body: $errorBody")
                    _chatResponse.value = AiResult.Error("Gagal mendapatkan respons (Code: ${response.code()})")
                }
            } catch (e: Exception) {
                // Tangani error jaringan atau exception lainnya
                Log.e("COHERE_API_ERROR", "Exception: ${e.message}", e)
                _chatResponse.value = AiResult.Error(e.message ?: "Terjadi kesalahan tidak diketahui.")
            }
        }
    }
}
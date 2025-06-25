package com.example.projectmdp.repository

import android.content.Context
import com.example.projectmdp.api.ApiService
import com.example.projectmdp.api.CreateDonationRequest
import com.example.projectmdp.api.Donation
import com.example.projectmdp.api.RetrofitClient
import com.example.projectmdp.utils.SessionManager

class DonationRepository(context: Context) {

    private val apiService: ApiService = RetrofitClient.instance

    private val authToken = "Bearer ${SessionManager.getAuthToken(context)}"

    suspend fun getAllDonations(): ResultWrapper<List<Donation>> {
        return try {
            val response = apiService.getAllDonations()
            if (response.isSuccessful && response.body() != null) {
                ResultWrapper.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Gagal memuat data donasi"
                ResultWrapper.Error(errorMsg)
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Terjadi kesalahan tidak diketahui")
        }
    }

    suspend fun createDonation(request: CreateDonationRequest): ResultWrapper<Donation> {
        return try {
            val response = apiService.createDonation(authToken, request)
            if (response.isSuccessful && response.body() != null) {
                ResultWrapper.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Gagal membuat donasi"
                ResultWrapper.Error(errorMsg)
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Terjadi kesalahan tidak diketahui")
        }
    }
}
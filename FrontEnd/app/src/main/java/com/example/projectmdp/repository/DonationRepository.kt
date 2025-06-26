package com.example.projectmdp.repository

import android.content.Context
import com.example.projectmdp.api.ApiService
import com.example.projectmdp.api.CreateDonationRequest
import com.example.projectmdp.api.Donation
import com.example.projectmdp.api.GenericResponse
import com.example.projectmdp.api.MakeDonationRequest
import com.example.projectmdp.api.RetrofitClient
import com.example.projectmdp.utils.SessionManager

/**
 * Repository untuk mengelola semua operasi data yang berhubungan dengan donasi.
 * Bertanggung jawab untuk berkomunikasi dengan ApiService.
 */
class DonationRepository(private val context: Context) {

    private val apiService: ApiService = RetrofitClient.instance

    /**
     * Helper function untuk mendapatkan token otentikasi dengan format "Bearer ".
     */
    private fun getAuthToken(): String {
        val token = SessionManager.getAuthToken(context)
        return "Bearer $token"
    }

    /**
     * Mengambil semua data donasi yang aktif dari server.
     */
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
            ResultWrapper.Error(e.message ?: "Terjadi kesalahan jaringan")
        }
    }

    /**
     * Mengambil detail satu donasi berdasarkan ID-nya.
     */
    suspend fun getDonationById(id: String): ResultWrapper<Donation> {
        return try {
            val response = apiService.getDonationById(id)
            if (response.isSuccessful && response.body() != null) {
                ResultWrapper.Success(response.body()!!)
            } else {
                ResultWrapper.Error("Donasi tidak ditemukan (ID: $id)")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Terjadi kesalahan jaringan")
        }
    }

    /**
     * Mengirim permintaan untuk membuat donasi baru. Memerlukan otentikasi.
     */
    suspend fun createDonation(request: CreateDonationRequest): ResultWrapper<Donation> {
        return try {
            val response = apiService.createDonation(getAuthToken(), request)
            if (response.isSuccessful && response.body() != null) {
                ResultWrapper.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Gagal membuat donasi"
                ResultWrapper.Error(errorMsg)
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Terjadi kesalahan jaringan")
        }
    }

    /**
     * Mengirim permintaan untuk melakukan donasi (transaksi). Memerlukan otentikasi.
     */
    suspend fun makeDonation(id: String, request: MakeDonationRequest): ResultWrapper<Donation> {
        return try {
            val response = apiService.makeDonation(id, getAuthToken(), request)
            if (response.isSuccessful && response.body() != null) {
                ResultWrapper.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Gagal melakukan donasi"
                ResultWrapper.Error(errorMsg)
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Terjadi kesalahan jaringan")
        }
    }

    /**
     * Mengirim permintaan untuk menghentikan penggalangan dana. Memerlukan otentikasi.
     */
    suspend fun stopDonation(id: String): ResultWrapper<GenericResponse> {
        return try {
            val response = apiService.stopDonation(id, getAuthToken())
            if (response.isSuccessful && response.body() != null) {
                ResultWrapper.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Gagal menghentikan donasi"
                ResultWrapper.Error(errorMsg)
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Terjadi kesalahan jaringan")
        }
    }
}
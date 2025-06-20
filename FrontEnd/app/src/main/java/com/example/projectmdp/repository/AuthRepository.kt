package com.example.projectmdp.repository

import android.content.Context
import com.example.projectmdp.api.*
import com.example.projectmdp.db.AppDatabase
import com.example.projectmdp.db.UserEntity

class AuthRepository(context: Context) {

    private val apiService = RetrofitClient.instance
    private val userDao = AppDatabase.getDatabase(context).userDao()

    // Fungsi untuk registrasi
    suspend fun register(request: RegisterRequest): ResultWrapper<GenericResponse> {
        return try {
            val response = apiService.registerUser(request)
            if (response.isSuccessful && response.body() != null) {
                ResultWrapper.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Registrasi API gagal"
                ResultWrapper.Error(errorMsg)
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Kesalahan jaringan")
        }
    }

    // Fungsi untuk login dan menyimpan data ke Room
    suspend fun loginAndCacheUser(request: LoginRequest): ResultWrapper<LoginResponse> {
        return try {
            val response = apiService.loginUser(request)
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                // Simpan data user ke database Room setelah login berhasil
                cacheUser(loginResponse.user)
                ResultWrapper.Success(loginResponse)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Login API gagal"
                ResultWrapper.Error(errorMsg)
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Kesalahan jaringan")
        }
    }

    // Fungsi untuk menyimpan user ke Room
    private suspend fun cacheUser(userData: UserData) {
        val userEntity = UserEntity(
            serverId = userData.id,
            customId = userData.customId ?: "",
            namaLengkap = userData.namaLengkap,
            email = userData.email,
            role = userData.role
        )
        userDao.insertUser(userEntity)
    }

    // Fungsi untuk mendapatkan user dari Room
    suspend fun getCachedUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
}

// Wrapper class untuk hasil, agar lebih rapi
sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val message: String) : ResultWrapper<Nothing>()
}
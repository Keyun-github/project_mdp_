package com.example.projectmdp.repository

import android.content.Context
import com.example.projectmdp.api.ApiService
import com.example.projectmdp.api.GenericResponse
import com.example.projectmdp.api.LoginRequest
import com.example.projectmdp.api.LoginResponse
import com.example.projectmdp.api.RegisterRequest
import com.example.projectmdp.api.RetrofitClient
import com.example.projectmdp.api.UserData
import com.example.projectmdp.db.AppDatabase
import com.example.projectmdp.db.UserDao
import com.example.projectmdp.db.UserEntity

class AuthRepository(context: Context) {

    private val apiService: ApiService = RetrofitClient.instance
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()

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

    suspend fun getCachedUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
}
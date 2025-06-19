package com.example.projectmdp.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmdp.api.*
import kotlinx.coroutines.launch

// Wrapper class untuk state management
sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
    object Loading : AuthResult<Nothing>()
}

class AuthViewModel : ViewModel() {

    private val _registrationResult = MutableLiveData<AuthResult<GenericResponse>>()
    val registrationResult: LiveData<AuthResult<GenericResponse>> = _registrationResult

    private val _loginResult = MutableLiveData<AuthResult<LoginResponse>>()
    val loginResult: LiveData<AuthResult<LoginResponse>> = _loginResult

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            _registrationResult.value = AuthResult.Loading
            try {
                val response = RetrofitClient.instance.registerUser(request)
                if (response.isSuccessful && response.body() != null) {
                    _registrationResult.value = AuthResult.Success(response.body()!!)
                } else {
                    // Coba parsing error body jika ada
                    val errorMsg = response.errorBody()?.string() ?: "Registrasi gagal"
                    _registrationResult.value = AuthResult.Error(errorMsg)
                }
            } catch (e: Exception) {
                _registrationResult.value = AuthResult.Error(e.message ?: "Terjadi kesalahan jaringan")
            }
        }
    }

    fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            _loginResult.value = AuthResult.Loading
            try {
                val response = RetrofitClient.instance.loginUser(request)
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = AuthResult.Success(response.body()!!)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Login gagal"
                    _loginResult.value = AuthResult.Error(errorMsg)
                }
            } catch (e: Exception) {
                _loginResult.value = AuthResult.Error(e.message ?: "Terjadi kesalahan jaringan")
            }
        }
    }
}
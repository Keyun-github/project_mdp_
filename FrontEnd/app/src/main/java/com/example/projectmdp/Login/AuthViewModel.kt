package com.example.projectmdp.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmdp.api.GenericResponse
import com.example.projectmdp.api.LoginRequest
import com.example.projectmdp.api.LoginResponse
import com.example.projectmdp.api.RegisterRequest
import com.example.projectmdp.repository.AuthRepository
import com.example.projectmdp.repository.ResultWrapper
import kotlinx.coroutines.launch

// Sealed class untuk merepresentasikan state UI
sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<ApiResult<GenericResponse>>()
    val registrationResult: LiveData<ApiResult<GenericResponse>> = _registrationResult

    private val _loginResult = MutableLiveData<ApiResult<LoginResponse>>()
    val loginResult: LiveData<ApiResult<LoginResponse>> = _loginResult

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            _registrationResult.value = ApiResult.Loading

            val result = repository.register(request)

            if (result is ResultWrapper.Success) {
                _registrationResult.value = ApiResult.Success(result.data)
                // Lakukan login otomatis di background untuk menyimpan data ke Room
                val loginRequest = LoginRequest(request.email, request.password)
                repository.loginAndCacheUser(loginRequest)
            } else if (result is ResultWrapper.Error) {
                _registrationResult.value = ApiResult.Error(result.message)
            }
        }
    }

    fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            _loginResult.value = ApiResult.Loading

            val result = repository.loginAndCacheUser(request)

            if (result is ResultWrapper.Success) {
                _loginResult.value = ApiResult.Success(result.data)
            } else if (result is ResultWrapper.Error) {
                _loginResult.value = ApiResult.Error(result.message)
            }
        }
    }
}
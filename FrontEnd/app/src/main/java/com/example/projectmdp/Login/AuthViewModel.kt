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

// Ganti nama AuthResult menjadi ApiResult agar lebih jelas
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
            when (val result = repository.register(request)) {
                is ResultWrapper.Success -> {
                    _registrationResult.value = ApiResult.Success(result.data)
                    // Setelah registrasi sukses, kita login diam-diam untuk dapat data user
                    // dan menyimpannya ke Room.
                    val loginRequest = LoginRequest(request.email, request.password)
                    repository.loginAndCacheUser(loginRequest) // Ini akan menyimpan ke Room
                }
                is ResultWrapper.Error -> {
                    _registrationResult.value = ApiResult.Error(result.message)
                }
            }
        }
    }

    fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            _loginResult.value = ApiResult.Loading
            when (val result = repository.loginAndCacheUser(request)) {
                is ResultWrapper.Success -> _loginResult.value = ApiResult.Success(result.data)
                is ResultWrapper.Error -> _loginResult.value = ApiResult.Error(result.message)
            }
        }
    }
}
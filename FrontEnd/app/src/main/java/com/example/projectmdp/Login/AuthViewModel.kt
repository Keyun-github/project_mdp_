package com.example.projectmdp.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmdp.api.*
import com.example.projectmdp.repository.AuthRepository
import com.example.projectmdp.repository.ResultWrapper
import kotlinx.coroutines.launch

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

    private val _updateProfileResult = MutableLiveData<ApiResult<LoginResponse>>()
    val updateProfileResult: LiveData<ApiResult<LoginResponse>> = _updateProfileResult

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            _registrationResult.value = ApiResult.Loading
            val result = repository.register(request)
            if (result is ResultWrapper.Success) {
                _registrationResult.value = ApiResult.Success(result.data)
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

    fun updateUserProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateProfileResult.value = ApiResult.Loading
            val result = repository.updateProfile(request)
            if (result is ResultWrapper.Success) {
                _updateProfileResult.value = ApiResult.Success(result.data)
            } else if (result is ResultWrapper.Error) {
                _updateProfileResult.value = ApiResult.Error(result.message)
            }
        }
    }
}
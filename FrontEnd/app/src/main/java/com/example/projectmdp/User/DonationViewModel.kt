package com.example.projectmdp.User

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmdp.Login.ApiResult // Pastikan impor ini benar
import com.example.projectmdp.api.CreateDonationRequest
import com.example.projectmdp.api.Donation
import com.example.projectmdp.repository.DonationRepository
import com.example.projectmdp.repository.ResultWrapper
import kotlinx.coroutines.launch

class DonationViewModel(private val repository: DonationRepository) : ViewModel() {

    private val _donations = MutableLiveData<ApiResult<List<Donation>>>()
    val donations: LiveData<ApiResult<List<Donation>>> = _donations

    private val _createStatus = MutableLiveData<ApiResult<Donation>>()
    val createStatus: LiveData<ApiResult<Donation>> = _createStatus

    fun fetchAllDonations() {
        viewModelScope.launch {
            _donations.value = ApiResult.Loading

            val result = repository.getAllDonations()

            if (result is ResultWrapper.Success) {
                _donations.value = ApiResult.Success(result.data)
            } else if (result is ResultWrapper.Error) {
                _donations.value = ApiResult.Error(result.message)
            }
        }
    }

    fun addNewDonation(request: CreateDonationRequest) {
        viewModelScope.launch {
            _createStatus.value = ApiResult.Loading

            val result = repository.createDonation(request)

            if (result is ResultWrapper.Success) {
                _createStatus.value = ApiResult.Success(result.data)
            } else if (result is ResultWrapper.Error) {
                _createStatus.value = ApiResult.Error(result.message)
            }
        }
    }
}
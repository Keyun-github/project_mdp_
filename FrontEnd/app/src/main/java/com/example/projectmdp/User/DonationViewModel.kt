package com.example.projectmdp.User

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmdp.Login.ApiResult
import com.example.projectmdp.api.CreateDonationRequest
import com.example.projectmdp.api.Donation
import com.example.projectmdp.api.GenericResponse
import com.example.projectmdp.api.MakeDonationRequest
import com.example.projectmdp.repository.DonationRepository
import com.example.projectmdp.repository.ResultWrapper
import kotlinx.coroutines.launch

/**
 * ViewModel untuk menangani semua logika bisnis yang berhubungan dengan data donasi.
 * Terhubung dengan DonationRepository untuk mengambil dan mengirim data.
 */
class DonationViewModel(private val repository: DonationRepository) : ViewModel() {

    // LiveData untuk daftar semua donasi yang akan ditampilkan di home
    private val _donations = MutableLiveData<ApiResult<List<Donation>>>()
    val donations: LiveData<ApiResult<List<Donation>>> = _donations

    // LiveData untuk detail satu donasi spesifik
    private val _donationDetail = MutableLiveData<ApiResult<Donation>>()
    val donationDetail: LiveData<ApiResult<Donation>> = _donationDetail

    // LiveData untuk status proses pembuatan donasi baru
    private val _createStatus = MutableLiveData<ApiResult<Donation>>()
    val createStatus: LiveData<ApiResult<Donation>> = _createStatus

    // LiveData untuk status proses pengiriman donasi (transaksi)
    private val _transactionStatus = MutableLiveData<ApiResult<Donation>>()
    val transactionStatus: LiveData<ApiResult<Donation>> = _transactionStatus

    // LiveData untuk status proses penghentian donasi
    private val _stopStatus = MutableLiveData<ApiResult<GenericResponse>>()
    val stopStatus: LiveData<ApiResult<GenericResponse>> = _stopStatus

    /**
     * Meminta data semua donasi dari repository dan memperbarui LiveData _donations.
     */
    fun fetchAllDonations() {
        viewModelScope.launch {
            _donations.value = ApiResult.Loading
            when (val result = repository.getAllDonations()) {
                is ResultWrapper.Success -> {
                    _donations.value = ApiResult.Success(result.data)
                }
                is ResultWrapper.Error -> {
                    _donations.value = ApiResult.Error(result.message)
                }
            }
        }
    }

    /**
     * Meminta detail satu donasi dari repository berdasarkan ID.
     */
    fun fetchDonationDetail(id: String) {
        viewModelScope.launch {
            _donationDetail.value = ApiResult.Loading
            when (val result = repository.getDonationById(id)) {
                is ResultWrapper.Success -> _donationDetail.value = ApiResult.Success(result.data)
                is ResultWrapper.Error -> _donationDetail.value = ApiResult.Error(result.message)
            }
        }
    }

    /**
     * Meminta untuk membuat donasi baru melalui repository.
     */
    fun addNewDonation(request: CreateDonationRequest) {
        viewModelScope.launch {
            _createStatus.value = ApiResult.Loading
            when (val result = repository.createDonation(request)) {
                is ResultWrapper.Success -> _createStatus.value = ApiResult.Success(result.data)
                is ResultWrapper.Error -> _createStatus.value = ApiResult.Error(result.message)
            }
        }
    }

    /**
     * Mengirimkan donasi (transaksi) untuk donasi tertentu.
     */
    fun submitDonation(id: String, request: MakeDonationRequest) {
        viewModelScope.launch {
            _transactionStatus.value = ApiResult.Loading
            when (val result = repository.makeDonation(id, request)) {
                is ResultWrapper.Success -> {
                    _transactionStatus.value = ApiResult.Success(result.data)
                    // Setelah donasi berhasil, refresh data detail agar progress bar terupdate
                    fetchDonationDetail(id)
                }
                is ResultWrapper.Error -> _transactionStatus.value = ApiResult.Error(result.message)
            }
        }
    }

    /**
     * Menghentikan penggalangan dana untuk donasi tertentu.
     */
    fun stopDonation(id: String) {
        viewModelScope.launch {
            _stopStatus.value = ApiResult.Loading
            when (val result = repository.stopDonation(id)) {
                is ResultWrapper.Success -> {
                    _stopStatus.value = ApiResult.Success(result.data)
                    // Setelah donasi dihentikan, refresh data detail agar statusnya terupdate
                    fetchDonationDetail(id)
                }
                is ResultWrapper.Error -> _stopStatus.value = ApiResult.Error(result.message)
            }
        }
    }
}
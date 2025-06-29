package com.example.projectmdp.utils

object EditProfileValidationUtils {

    fun validateEditProfileInput(
        namaLengkap: String,
        oldPassword: String,
        newPassword: String
    ): String? {
        if (namaLengkap.isEmpty()) {
            return "Nama tidak boleh kosong"
        }

        if (newPassword.isNotEmpty() && oldPassword.isEmpty()) {
            return "Masukkan password lama untuk mengganti password"
        }

        return null // valid
    }
}
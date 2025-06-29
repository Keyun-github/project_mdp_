package com.example.projectmdp.utils

object DonationValidationUtils {
    fun validateDonationInput(title: String, target: String): String? {
        if (title.isEmpty() || target.isEmpty()) {
            return "Semua field harus diisi"
        }

        val targetLong = target.toLongOrNull()
        if (targetLong == null || targetLong <= 0) {
            return "Target donasi harus berupa angka dan lebih dari 0"
        }

        return null // valid
    }
}
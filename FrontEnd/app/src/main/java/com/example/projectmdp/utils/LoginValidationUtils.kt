package com.example.projectmdp.utils

object LoginValidationUtils {
    fun validateLoginInput(email: String, password: String): String? {
        if (email.isEmpty() || password.isEmpty()) {
            return "Email dan Password tidak boleh kosong"
        }

        return null // Artinya valid
    }
}

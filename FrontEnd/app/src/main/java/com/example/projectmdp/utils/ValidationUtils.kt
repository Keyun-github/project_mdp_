package com.example.projectmdp.utils

object ValidationUtils {

    fun validateRegisterInput(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Harap lengkapi semua data"
        }

        if (password.length < 6) {
            return "Password minimal harus 6 karakter"
        }

        if (password != confirmPassword) {
            return "Password dan konfirmasi tidak cocok"
        }

        return null // berarti valid
    }
}

package com.example.projectmdp


import com.example.projectmdp.utils.ValidationUtils
import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun testEmptyFields() {
        val result =
            ValidationUtils.validateRegisterInput("", "email@example.com", "123456", "123456")
        assertEquals("Harap lengkapi semua data", result)
    }

    @Test
    fun testShortPassword() {
        val result =
            ValidationUtils.validateRegisterInput("Michael", "email@example.com", "123", "123")
        assertEquals("Password minimal harus 6 karakter", result)
    }

    @Test
    fun testPasswordMismatch() {
        val result = ValidationUtils.validateRegisterInput(
            "Michael",
            "email@example.com",
            "123456",
            "654321"
        )
        assertEquals("Password dan konfirmasi tidak cocok", result)
    }

    @Test
    fun testValidInput() {
        val result = ValidationUtils.validateRegisterInput(
            "Michael",
            "email@example.com",
            "123456",
            "123456"
        )
        assertNull(result)
    }
}

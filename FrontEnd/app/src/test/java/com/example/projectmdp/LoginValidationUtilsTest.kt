package com.example.projectmdp

import com.example.projectmdp.utils.LoginValidationUtils
import org.junit.Assert.*
import org.junit.Test

class LoginValidationUtilsTest {

    @Test
    fun testEmptyEmail() {
        val result = LoginValidationUtils.validateLoginInput("", "password123")
        assertEquals("Email dan Password tidak boleh kosong", result)
    }

    @Test
    fun testEmptyPassword() {
        val result = LoginValidationUtils.validateLoginInput("email@example.com", "")
        assertEquals("Email dan Password tidak boleh kosong", result)
    }

    @Test
    fun testEmptyEmailAndPassword() {
        val result = LoginValidationUtils.validateLoginInput("", "")
        assertEquals("Email dan Password tidak boleh kosong", result)
    }

    @Test
    fun testValidInput() {
        val result = LoginValidationUtils.validateLoginInput("email@example.com", "password123")
        assertNull(result)
    }
}

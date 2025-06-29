package com.example.projectmdp

import com.example.projectmdp.utils.EditProfileValidationUtils
import org.junit.Assert.*
import org.junit.Test

class EditProfileValidationUtilsTest {

    @Test
    fun testEmptyName() {
        val result = EditProfileValidationUtils.validateEditProfileInput("", "", "")
        assertEquals("Nama tidak boleh kosong", result)
    }

    @Test
    fun testChangingPasswordWithoutOldPassword() {
        val result = EditProfileValidationUtils.validateEditProfileInput("Michael", "", "newPass123")
        assertEquals("Masukkan password lama untuk mengganti password", result)
    }

    @Test
    fun testValidWithoutPasswordChange() {
        val result = EditProfileValidationUtils.validateEditProfileInput("Michael", "", "")
        assertNull(result)
    }

    @Test
    fun testValidWithPasswordChange() {
        val result = EditProfileValidationUtils.validateEditProfileInput("Michael", "oldPass123", "newPass123")
        assertNull(result)
    }
}

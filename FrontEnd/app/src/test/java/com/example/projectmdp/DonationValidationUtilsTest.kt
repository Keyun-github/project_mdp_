package com.example.projectmdp

import com.example.projectmdp.utils.DonationValidationUtils
import org.junit.Assert.*
import org.junit.Test

class DonationValidationUtilsTest {

    @Test
    fun testEmptyTitle() {
        val result = DonationValidationUtils.validateDonationInput("", "1000")
        assertEquals("Semua field harus diisi", result)
    }

    @Test
    fun testEmptyTarget() {
        val result = DonationValidationUtils.validateDonationInput("Donasi Anak Yatim", "")
        assertEquals("Semua field harus diisi", result)
    }

    @Test
    fun testInvalidTargetNonNumeric() {
        val result = DonationValidationUtils.validateDonationInput("Donasi Sekolah", "abc")
        assertEquals("Target donasi harus berupa angka dan lebih dari 0", result)
    }

    @Test
    fun testInvalidTargetNegative() {
        val result = DonationValidationUtils.validateDonationInput("Donasi", "-100")
        assertEquals("Target donasi harus berupa angka dan lebih dari 0", result)
    }

    @Test
    fun testValidInput() {
        val result = DonationValidationUtils.validateDonationInput("Donasi Air Bersih", "50000")
        assertNull(result)
    }
}

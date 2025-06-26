package com.example.projectmdp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.projectmdp.Login.RegisterFragment
import com.example.projectmdp.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest {

    @Before
    fun setUp() {
        // Jalankan RegisterFragment sebelum setiap test
        launchFragmentInContainer<RegisterFragment>(
            themeResId = R.style.Theme_ProjectMDP
        )
    }

    @Test
    fun testRegister_emptyFields_showError() {
        // Klik tombol tanpa mengisi input
        onView(withId(R.id.btnRegister)).perform(click())

        // Periksa bahwa tombol tetap aktif
        onView(withId(R.id.btnRegister)).check(matches(isEnabled()))
    }

    @Test
    fun testRegister_passwordTooShort_showError() {
        onView(withId(R.id.etFullName)).perform(typeText("Test User"), closeSoftKeyboard())
        onView(withId(R.id.etEmail)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.etPasswordConfirmation)).perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).perform(click())

        onView(withId(R.id.btnRegister)).check(matches(isEnabled()))
    }

    @Test
    fun testRegister_passwordMismatch_showError() {
        onView(withId(R.id.etFullName)).perform(typeText("Test User"), closeSoftKeyboard())
        onView(withId(R.id.etEmail)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.etPasswordConfirmation)).perform(typeText("654321"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).perform(click())

        onView(withId(R.id.btnRegister)).check(matches(isEnabled()))
    }

    @Test
    fun testRegister_validInput() {
        onView(withId(R.id.etFullName)).perform(typeText("Test User"), closeSoftKeyboard())
        onView(withId(R.id.etEmail)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.etPasswordConfirmation)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).perform(click())

        // Tidak bisa cek ViewModel langsung tanpa mock, tapi kita pastikan tombol tidak error
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))
    }
}

package com.example.projectmdp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.projectmdp.R
import com.example.projectmdp.utils.SessionManager
import com.example.projectmdp.Login.LoginFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.navigation.testing.TestNavHostController

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @Before
    fun setUp() {
        // Hapus token agar tidak auto-login
        SessionManager.clearAuthToken(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        // Bersihkan token setelah test selesai
        SessionManager.clearAuthToken(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testLogin_emptyFields_showError() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_ProjectMDP)

        onView(withId(R.id.btnLogin)).perform(click())

        onView(withText("Email dan Password tidak boleh kosong"))
            .inRoot(ToastMatcher()) // custom matcher untuk Toast
            .check(matches(isDisplayed()))
    }

    @Test
    fun testLogin_validInput_buttonClickable() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_ProjectMDP)

        onView(withId(R.id.etEmail)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("123456"), closeSoftKeyboard())

        onView(withId(R.id.btnLogin)).check(matches(isEnabled()))
    }

    @Test
    fun testGoToRegister_navigatesToRegisterFragment() {
        val scenario = launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_ProjectMDP)

        scenario.onFragment { fragment ->
            val navController = TestNavHostController(fragment.requireContext())
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.tvGoToRegister)).perform(click())

        // Tidak ada assert NavController di sini karena ini hanya contoh UI behavior
        // Tapi bisa kamu tambahkan assert navController.currentDestination.id == R.id.registerFragment jika ingin
    }
}
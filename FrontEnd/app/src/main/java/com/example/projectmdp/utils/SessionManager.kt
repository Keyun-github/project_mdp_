package com.example.projectmdp.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Objek helper untuk mengelola data sesi pengguna seperti token otentikasi
 * dan informasi pengguna lainnya menggunakan SharedPreferences.
 */
object SessionManager {
    private const val PREF_NAME = "AppSession"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email" // Tambahan untuk menampilkan email

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // --- FUNGSI UNTUK MENYIMPAN DATA ---
    fun saveAuthToken(context: Context, token: String) {
        getPreferences(context).edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun saveUserName(context: Context, name: String) {
        getPreferences(context).edit().putString(KEY_USER_NAME, name).apply()
    }

    fun saveUserEmail(context: Context, email: String) {
        getPreferences(context).edit().putString(KEY_USER_EMAIL, email).apply()
    }

    // --- FUNGSI UNTUK MENGAMBIL DATA ---
    fun getAuthToken(context: Context): String? {
        return getPreferences(context).getString(KEY_AUTH_TOKEN, null)
    }

    fun getUserName(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_NAME, null)
    }

    fun getUserEmail(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_EMAIL, null)
    }

    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear() // Menghapus semua data dari SharedPreferences ini
        editor.apply()
    }
}
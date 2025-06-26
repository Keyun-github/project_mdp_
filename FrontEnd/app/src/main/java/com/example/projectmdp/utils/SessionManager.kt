package com.example.projectmdp.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "AppUserSession"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveAuthToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.apply()
    }

    fun saveUserId(context: Context, userId: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }

    fun saveUserName(context: Context, name: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_USER_NAME, name)
        editor.apply()
    }

    fun saveUserEmail(context: Context, email: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_USER_EMAIL, email)
        editor.apply()
    }

    fun getAuthToken(context: Context): String? {
        return getPreferences(context).getString(KEY_AUTH_TOKEN, null)
    }

    fun getUserId(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_ID, null)
    }

    fun getUserName(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_NAME, null)
    }

    fun getUserEmail(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_EMAIL, null)
    }

    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear() // Menghapus semua data dari file SharedPreferences ini
        editor.apply()
    }
}
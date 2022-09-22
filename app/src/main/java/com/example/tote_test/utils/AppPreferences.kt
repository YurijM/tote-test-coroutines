package com.example.tote_test.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val IS_AUTH = "isAuth"
    private const val NAME_PREF = "preference"

    private lateinit var preferences: SharedPreferences

    fun getPreferences(context: Context): SharedPreferences {
        preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE)
        return preferences
    }

    fun getIsAuth(): Boolean {
        return preferences.getBoolean(IS_AUTH, false)
    }

    fun setIsAuth(isAuth: Boolean) {
        preferences.edit()
            .putBoolean(IS_AUTH, isAuth)
            .apply()
    }
}
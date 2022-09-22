package com.example.tote_test.utils

import com.example.tote_test.R

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: APP_ACTIVITY.resources.getString(R.string.unknown_error))
    }
}
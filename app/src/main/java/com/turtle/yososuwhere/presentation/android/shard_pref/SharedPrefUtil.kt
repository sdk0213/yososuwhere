package com.turtle.yososuwhere.presentation.android.shard_pref

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefUtil @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) {

    companion object {
        private const val IS_LOGGED_IN = "pref_is_logged_in"
    }

    var isLoggedDevices: Boolean
        get() = sharedPreferences.getBoolean(IS_LOGGED_IN, false)
        set(value) {
            editor.putBoolean(IS_LOGGED_IN, value)
            editor.apply()
        }

}
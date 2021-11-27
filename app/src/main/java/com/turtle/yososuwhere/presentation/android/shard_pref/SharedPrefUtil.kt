package com.turtle.yososuwhere.presentation.android.shard_pref

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefUtil @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) {

    companion object {
        private const val prefUseFilterByHasStock = "pref_use_filter_by_has_stock"
    }

    var useFilterByHasStock: Boolean
        get() = sharedPreferences.getBoolean(prefUseFilterByHasStock, false)
        set(value) {
            editor.putBoolean(prefUseFilterByHasStock, value)
            editor.apply()
        }

}
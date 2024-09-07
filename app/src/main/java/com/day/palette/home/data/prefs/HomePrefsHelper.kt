package com.day.palette.home.data.prefs

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class HomePrefsHelper @Inject constructor(context: Context) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var selectedCountryName: String?
        get() = sharedPrefs.getString(PREF_SELECTED_COUNTRY_NAME, null)
        set(value) = sharedPrefs.edit().putString(PREF_SELECTED_COUNTRY_NAME, value).apply()

    var selectedCountryCode: String?
        get() = sharedPrefs.getString(PREF_SELECTED_COUNTRY_CODE, null)
        set(value) = sharedPrefs.edit().putString(PREF_SELECTED_COUNTRY_CODE, value).apply()

    companion object {
        const val PREFS_NAME = "day_palette_user_prefs"
        const val PREF_SELECTED_COUNTRY_NAME = "selected_country_name"
        const val PREF_SELECTED_COUNTRY_CODE = "selected_country_code"
    }
}
package com.example.weatherapp.data.repository

import android.content.Context
import javax.inject.Inject

class PrefRepository @Inject constructor(context: Context) {

    private val PREFERENCE_NAME = "shared_preference"
    private val KEY_LAST_INPUT = "text_input"

    private val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setUserInput(city: String) {
        pref.edit()
            .putString(KEY_LAST_INPUT, city)
            .apply()
    }

    fun getUserInput() = pref.getString(KEY_LAST_INPUT, "")
}
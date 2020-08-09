package com.zavsmit.jokes.data

import android.app.Application
import android.content.Context
import androidx.core.content.edit

class SharedPrefsHelper constructor(application: Application) {
    companion object {
        private val PREF_FILE_NAME = "android_jokes_pref_file"
        private val FIRST_NAME = "first_name"
        private val LAST_NAME = "last_name"
        private val IS_OFFLINE_MODE = "IS_OFFLINE_MODE"
    }

    private var pref = application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    fun saveFirstName(firstName: String) {
        pref.edit { putString(FIRST_NAME, firstName) }
    }

    fun saveLastName(lastName: String) {
        pref.edit { putString(LAST_NAME, lastName) }
    }

    fun getFirstName() = pref.getString(FIRST_NAME, "") ?: ""
    fun getLastName() = pref.getString(LAST_NAME, "") ?: ""

    fun setOfflineMode(isOffline: Boolean) {
        pref.edit { putBoolean(IS_OFFLINE_MODE, isOffline) }
    }

    fun isOfflineMode() = pref.getBoolean(IS_OFFLINE_MODE, false)
}
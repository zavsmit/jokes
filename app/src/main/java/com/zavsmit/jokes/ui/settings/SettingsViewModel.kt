package com.zavsmit.jokes.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zavsmit.jokes.data.SharedPrefsHelper

class SettingsViewModel @ViewModelInject constructor(private val sharedPref: SharedPrefsHelper) : ViewModel() {
    private val _uiModel = MutableLiveData<UiSettingsModel>()
    val uiModel: LiveData<UiSettingsModel> = _uiModel

    fun setData(firstName: String, lastName: String, isOffline: Boolean) {
        sharedPref.setOfflineMode(isOffline)
        sharedPref.setName(firstName, lastName)
    }

    fun getData() {
        val isOfflineMode = sharedPref.isOfflineMode()
        val firstName = sharedPref.getFirstName()
        val lastName = sharedPref.getLastName()

        _uiModel.value = UiSettingsModel(firstName, lastName, isOfflineMode)
    }
}

data class UiSettingsModel(
        val firstName: String = "",
        val lastName: String = "",
        val isOffline: Boolean = false
)
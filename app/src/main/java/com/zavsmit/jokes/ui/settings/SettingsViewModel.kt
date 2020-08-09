package com.zavsmit.jokes.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zavsmit.jokes.data.SharedPrefsHelper
import com.zavsmit.jokes.domain.models.UiSettingsModel

class SettingsViewModel @ViewModelInject constructor(private val sharedPref: SharedPrefsHelper) : ViewModel() {
    private val _uiModel = MutableLiveData<UiSettingsModel>()
    val uiModel: LiveData<UiSettingsModel> = _uiModel

    fun saveFirstName(firstName: String) {
        sharedPref.saveFirstName(firstName)
    }

    fun saveLastName(lastName: String) {
        sharedPref.saveLastName(lastName)
    }

    fun getData() {
        val isOfflineMode = sharedPref.isOfflineMode()
        val firstName = sharedPref.getFirstName()
        val lastName = sharedPref.getLastName()

        _uiModel.value = UiSettingsModel(firstName, lastName, isOfflineMode)
    }

    fun setOfflineMode(check: Boolean) {
        sharedPref.setOfflineMode(check)
    }
}
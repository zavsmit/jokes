package com.zavsmit.jokes.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _uiModel = MutableLiveData<UiSettingsModel>().apply {
        value = UiSettingsModel()
    }
    val uiModel: LiveData<UiSettingsModel> = _uiModel
}


data class UiSettingsModel(
    val firstName: String = "",
    val lastName: String = "",
    val isOffline: Boolean = false
)
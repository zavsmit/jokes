package com.zavsmit.jokes.domain.models


data class UiSettingsModel(
        val firstName: String = "",
        val lastName: String = "",
        val isOffline: Boolean = false
)

data class UiJokes(
        val list: List<UiModelJoke>,
        val screenNumber: Int
)


data class UiModelJoke(
        val text: String,
        val likeButtonText: String,
        val id: Long = 0
)
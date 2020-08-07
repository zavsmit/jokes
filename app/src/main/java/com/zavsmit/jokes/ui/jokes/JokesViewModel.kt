package com.zavsmit.jokes.ui.jokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JokesListViewModel : ViewModel() {

    private val _uiJoke = MutableLiveData<List<UiModelJoke>>().apply {
        value = getListJokes()
    }
    val uiJoke: LiveData<List<UiModelJoke>> = _uiJoke


    private fun getListJokes(): List<UiModelJoke> {
        val listJokes = mutableListOf<UiModelJoke>()

        listJokes.add(UiModelJoke("1111111111111111", "Like"))
        listJokes.add(UiModelJoke("2222222222222222 asd sfasd asd fasdf asd fasd fasdjhfl aksdhfl sdyfhl kadhslkfu aksfaj dfgyhalskdfhla sglahjfkauy iluasdfglas ufasygdf ", "Dislike"))
        listJokes.add(UiModelJoke("3333333333333333", "Like"))

        return listJokes
    }
}

data class UiModelJoke(
        val text: String = "",
        val likeButtonText: String = "Like",
        val isVisibleShare: Boolean = true
)
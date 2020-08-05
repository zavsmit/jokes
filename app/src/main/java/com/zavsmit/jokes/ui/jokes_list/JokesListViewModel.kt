package com.zavsmit.jokes.ui.jokes_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JokesListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is jokes Fragment"
    }
    val text: LiveData<String> = _text
}
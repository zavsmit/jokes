package com.zavsmit.jokes.ui.my_jokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyJokesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my jokes Fragment"
    }
    val text: LiveData<String> = _text
}
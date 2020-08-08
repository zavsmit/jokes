package com.zavsmit.jokes.ui.jokes

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zavsmit.jokes.data.JokesRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class JokesViewModel @ViewModelInject constructor(private val repository: JokesRepository) : ViewModel() {

    private val _uiJoke = MutableLiveData<List<UiModelJoke>>()
    val uiJoke: LiveData<List<UiModelJoke>> = _uiJoke

    fun onLikeClicked(id: Long) {

    }

    fun onShareClicked(text: String) {

    }

    fun getData() {
        repository.getJokes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _uiJoke.value = result
                },
                        { t: Throwable? ->
                            Log.d("sdf", "sdf")
                        })
    }
}

data class UiModelJoke(
        val text: String = "",
        val likeButtonText: String = "Like",
        val isVisibleShare: Boolean = true,
        val id: Long = 0
)
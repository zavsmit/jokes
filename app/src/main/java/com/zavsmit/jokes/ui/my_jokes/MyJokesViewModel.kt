package com.zavsmit.jokes.ui.my_jokes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zavsmit.jokes.R
import com.zavsmit.jokes.core.SingleLiveEvent
import com.zavsmit.jokes.domain.JokesRepository
import com.zavsmit.jokes.domain.models.UiJokes
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment.Companion.DATA_SCREEN
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment.Companion.TEXT_SCREEN
import com.zavsmit.jokes.ui.jokes.ViewEffect
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MyJokesViewModel @ViewModelInject constructor(private val repository: JokesRepository) : ViewModel() {

    private val _uiJoke = MutableLiveData<UiJokes>()
    val uiJoke: LiveData<UiJokes> = _uiJoke

    private val _viewEffect = SingleLiveEvent<ViewEffect>()
    val viewEffect: LiveData<ViewEffect> = _viewEffect

    private val compositeDisposable = CompositeDisposable()

    init {
        getData()
    }

    fun setNewJoke(text: String) {
        if (text.isBlank()) return
        compositeDisposable.clear()
        compositeDisposable.add(
                repository.addMyJoke(text)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            getData()
                        }, {
                            showError()
                        }))
    }

    fun deleteJoke(id: Long) {
        compositeDisposable.clear()
        compositeDisposable.add(
                repository.deleteMyJoke(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            getData()
                            _viewEffect.postValue(ViewEffect.SnackBar(getString(R.string.joke_deleted)))
                        }, {
                            showError()
                        }))
    }

    fun getData() {
        compositeDisposable.clear()
        compositeDisposable.add(
                repository.getMyJokes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val screenType = if (it.isEmpty()) TEXT_SCREEN else DATA_SCREEN
                            _uiJoke.value = UiJokes(it, screenType)
                            _viewEffect.postValue(ViewEffect.Progress(false))
                        }, { t: Throwable? ->
                            showError()
                            _viewEffect.postValue(ViewEffect.Progress(false))
                        }))
    }

    fun onDestroy() {
        compositeDisposable.dispose()
    }

    private fun showError() {
        _viewEffect.postValue(ViewEffect.SnackBar(getString(R.string.error)))
    }

    private fun getString(id: Int) = repository.getResources().getString(id)
}
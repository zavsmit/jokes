package com.zavsmit.jokes.ui.jokes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.zavsmit.jokes.R
import com.zavsmit.jokes.core.SingleLiveEvent
import com.zavsmit.jokes.core.sensor.ShakeEventProvider
import com.zavsmit.jokes.data.ResourceManager
import com.zavsmit.jokes.data.SharedPrefsHelper
import com.zavsmit.jokes.data.db.models.JokeDb
import com.zavsmit.jokes.domain.models.UiJokes
import com.zavsmit.jokes.domain.models.UiModelJoke
import com.zavsmit.jokes.domain.usecases.*
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment.Companion.DATA_SCREEN
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class JokesViewModel @ViewModelInject constructor(
        private val resourceManager: ResourceManager,
        private val sharedPrefsHelper: SharedPrefsHelper,
        private val getJokesUseCase: GetJokesUseCase,
        private val addMyJokeByIdUseCase: AddMyJokeByIdUseCase,
        private val deleteJokeUseCase: DeleteJokeUseCase,
        private val getNextJokesUseCase: GetNextJokesUseCase,
        private val refreshJokeUseCase: RefreshJokeUseCase,
        private val getRandomJokeUseCase: GetRandomJokeUseCase,
        private val getMyJokeIdsUseCase: GetMyJokeIdsUseCase,
        private val shakeEventProvider: ShakeEventProvider) : ViewModel() {

    private val _uiJoke = MutableLiveData<UiJokes>()
    val uiJoke: LiveData<UiJokes> = _uiJoke

    private val _viewEffect = SingleLiveEvent<SingleEvent>()
    val singleEvent: LiveData<SingleEvent> = _viewEffect

    private val compositeDisposable = CompositeDisposable()
    private val shakeObserver = Observer<Boolean> { refreshData() }
    private var pageNumber = 0
    private var isLoadingData = false

    init {
        shakeEventProvider.apply {
            subscribe()
            observeForever(shakeObserver)
        }
        getFirstData()
    }

    fun onLikeClicked(id: Long) {
        val jokes = uiJoke.value?.list?.toMutableList()
        jokes ?: return

        for (joke in jokes) {
            if (joke.id == id) {
                val newTextButton = if (joke.likeButtonText == getString(R.string.like)) {
                    addMyJokeByIdUseCase.execute(AddMyJokeByIdUseCase.Arg(id))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { _viewEffect.value = SingleEvent.SnackBar(getString(R.string.joke_added)) }
                    getString(R.string.dislike)
                } else {
                    deleteJokeUseCase.execute(DeleteJokeUseCase.Arg(id))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { _viewEffect.postValue(SingleEvent.SnackBar(getString(R.string.joke_removed))) }
                    getString(R.string.like)
                }
                val newJoke = UiModelJoke(joke.text, newTextButton, joke.id)
                jokes.remove(joke)
                val newList = jokes.toMutableList() // Unintuitive way to copy a list
                newList.add(newJoke)
                newList.sortBy { it.id }
                _uiJoke.value = uiJoke.value?.copy(list = newList)

                break
            }
        }
    }

    fun onShareClicked(text: String) {
        _viewEffect.postValue(SingleEvent.Share(text))
    }

    private fun getFirstData() {
        compositeDisposable.clear()
        if (sharedPrefsHelper.isOfflineMode()) {
            getRandomJoke()
            return
        }

        _viewEffect.postValue(SingleEvent.Progress(true))
        compositeDisposable.addAll(
                getJokesUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .map {
                            val myJokeIds = getMyJokeIdsUseCase.execute(GetMyJokeIdsUseCase.Arg(it))
                            return@map mapToUiList(it, myJokeIds)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val screenType = if (it.isEmpty()) JokesParentFragment.TEXT_SCREEN else DATA_SCREEN
                            _uiJoke.value = UiJokes(it, screenType)
                            _viewEffect.value = SingleEvent.Progress(false)
                        }, { t: Throwable? ->
                            showError(t?.message)
                            _viewEffect.value = SingleEvent.Progress(false)
                        })
        )
    }

    fun refreshData() {
        compositeDisposable.clear()
        _viewEffect.postValue(SingleEvent.Progress(true))
        if (sharedPrefsHelper.isOfflineMode()) {
            getRandomJoke()
            return
        }

        pageNumber = 0
        compositeDisposable.addAll(
                refreshJokeUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .map {
                            val myJokeIds = getMyJokeIdsUseCase.execute(GetMyJokeIdsUseCase.Arg(it))
                            return@map mapToUiList(it, myJokeIds)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val screenType = if (it.isEmpty()) JokesParentFragment.TEXT_SCREEN else DATA_SCREEN
                            _uiJoke.value = UiJokes(it, screenType)
                            _viewEffect.postValue(SingleEvent.Progress(false))
                        }, { t: Throwable? ->
                            showError(t?.message)
                            _viewEffect.value = SingleEvent.Progress(false)
                        })
        )
    }

    fun getNextData() {
        compositeDisposable.clear()

        if (sharedPrefsHelper.isOfflineMode()) {
            getRandomJoke()
            return
        }

        if (isLoadingData) return
        isLoadingData = true
        ++pageNumber
        compositeDisposable.addAll(
                getNextJokesUseCase.execute(GetNextJokesUseCase.NextJokesArgsModel(pageNumber))
                        .subscribeOn(Schedulers.io())
                        .map {
                            val myJokeIds = getMyJokeIdsUseCase.execute(GetMyJokeIdsUseCase.Arg(it))
                            return@map mapToUiList(it, myJokeIds)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val changedList = uiJoke.value?.list?.toMutableList()
                            changedList?.addAll(it)
                            changedList?.let { newList ->
                                _uiJoke.value = _uiJoke.value?.copy(list = newList.toList())
                            }
                            isLoadingData = false
                        }, { t: Throwable? ->
                            showError(t?.message)
                            isLoadingData = false
                        })
        )
    }

    fun onDestroy() {
        compositeDisposable.dispose()
        shakeEventProvider.apply {
            removeObserver(shakeObserver)
            unsubscribe()
        }
    }

    private fun showError(text: String?) {
        _viewEffect.value = SingleEvent.SnackBar(text ?: getString(R.string.error))
    }

    private fun getRandomJoke() {
        compositeDisposable.addAll(
                getRandomJokeUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .map {
                            val myJokeIds = getMyJokeIdsUseCase.execute(GetMyJokeIdsUseCase.Arg(it))
                            return@map mapToUiList(it, myJokeIds)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val screenType = if (it.isEmpty()) JokesParentFragment.TEXT_SCREEN else DATA_SCREEN
                            _uiJoke.value = UiJokes(it, screenType)
                            _viewEffect.postValue(SingleEvent.Progress(false))
                        }, { t: Throwable? ->
                            showError(t?.message)
                            _viewEffect.value = SingleEvent.Progress(false)
                        })
        )
    }

    private fun mapToUiList(list: List<JokeDb>, myJokeIds: List<Long>): List<UiModelJoke> {
        return mutableListOf<UiModelJoke>().apply {
            list.forEach {
                val textButton = if (myJokeIds.contains(it.id))
                    resourceManager.getString(R.string.dislike)
                else resourceManager.getString(R.string.like)

                add(UiModelJoke(id = it.id, text = it.joke, likeButtonText = textButton))
            }
        }
    }

    private fun getString(id: Int) = resourceManager.getString(id)
}

sealed class SingleEvent {
    data class Share(val text: String) : SingleEvent()
    data class SnackBar(val text: String) : SingleEvent()
    data class Progress(val isVisible: Boolean) : SingleEvent()
}
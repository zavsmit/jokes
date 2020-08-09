package com.zavsmit.jokes.ui.my_jokes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zavsmit.jokes.R
import com.zavsmit.jokes.core.SingleLiveEvent
import com.zavsmit.jokes.data.ResourceManager
import com.zavsmit.jokes.data.SharedPrefsHelper
import com.zavsmit.jokes.data.db.models.MyJokeDb
import com.zavsmit.jokes.domain.models.UiJokes
import com.zavsmit.jokes.domain.models.UiModelJoke
import com.zavsmit.jokes.domain.usecases.AddJokeUseCase
import com.zavsmit.jokes.domain.usecases.DeleteJokeUseCase
import com.zavsmit.jokes.domain.usecases.GetMyJokesUseCase
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment.Companion.DATA_SCREEN
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment.Companion.TEXT_SCREEN
import com.zavsmit.jokes.ui.jokes.SingleEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class MyJokesViewModel @ViewModelInject constructor(
        private val resourceManager: ResourceManager,
        private val sharedPrefsHelper: SharedPrefsHelper,
        private val addJokeUseCase: AddJokeUseCase,
        private val deleteJokeUseCase: DeleteJokeUseCase,
        private val getMyJokesUseCase: GetMyJokesUseCase
) : ViewModel() {

    private val _uiJoke = MutableLiveData<UiJokes>()
    val uiJoke: LiveData<UiJokes> = _uiJoke

    private val _viewEffect = SingleLiveEvent<SingleEvent>()
    val singleEvent: LiveData<SingleEvent> = _viewEffect

    private val compositeDisposable = CompositeDisposable()

    init {
        getData()
    }

    fun setNewJoke(text: String) {
        if (text.isBlank()) return
        compositeDisposable.clear()
        compositeDisposable.add(
                addJokeUseCase.execute(AddJokeUseCase.Arg(text))
                        .andThen(getMyJokesUseCase.execute())
                        .map { return@map mapMyJokeToUiList(it) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            setResultToUi(it)
                        }, {
                            showError()
                        }))
    }

    fun deleteJoke(id: Long) {
        compositeDisposable.clear()
        compositeDisposable.add(
                deleteJokeUseCase.execute(DeleteJokeUseCase.Arg(id))
                        .andThen(getMyJokesUseCase.execute())
                        .map { return@map mapMyJokeToUiList(it) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            setResultToUi(it)
                            _viewEffect.postValue(SingleEvent.SnackBar(getString(R.string.joke_deleted)))
                        }, {
                            showError()
                        }))
    }

    fun getData() {
        compositeDisposable.clear()
        compositeDisposable.add(
                getMyJokesUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .map { return@map mapMyJokeToUiList(it) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            setResultToUi(it)
                            _viewEffect.postValue(SingleEvent.Progress(false))
                        }, { t: Throwable? ->
                            showError()
                            _viewEffect.postValue(SingleEvent.Progress(false))
                        }))
    }

    fun onDestroy() {
        compositeDisposable.dispose()
    }

    private fun mapMyJokeToUiList(list: List<MyJokeDb>): List<UiModelJoke> {
        val first = sharedPrefsHelper.getFirstName()
        val last = sharedPrefsHelper.getLastName()
        val firstName = if (first.isBlank()) resourceManager.getString(R.string.chuck) else first
        val lastName = if (last.isBlank()) resourceManager.getString(R.string.norris) else last

        return mutableListOf<UiModelJoke>().apply {
            list.forEach {
                var joke = it.joke
                joke = joke.replace(resourceManager.getString(R.string.chuck), firstName)
                joke = joke.replace(resourceManager.getString(R.string.norris), lastName)

                add(UiModelJoke(id = it.id, text = joke, likeButtonText = resourceManager.getString(R.string.delete)))
            }
        }
    }

    private fun setResultToUi(list: List<UiModelJoke>) {
        val screenType = if (list.isEmpty()) TEXT_SCREEN else DATA_SCREEN
        _uiJoke.value = UiJokes(list, screenType)
    }

    private fun showError() {
        _viewEffect.postValue(SingleEvent.SnackBar(getString(R.string.error)))
    }

    private fun getString(id: Int) = resourceManager.getString(id)
}
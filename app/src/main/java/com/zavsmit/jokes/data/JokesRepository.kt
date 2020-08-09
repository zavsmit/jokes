package com.zavsmit.jokes.data

import com.zavsmit.jokes.R
import com.zavsmit.jokes.core.ConnectivityState
import com.zavsmit.jokes.data.db.JokesDao
import com.zavsmit.jokes.data.db.models.JokeDb
import com.zavsmit.jokes.data.db.models.MyJokeDb
import com.zavsmit.jokes.data.network.JokesApi
import com.zavsmit.jokes.data.network.models.JokeItemResponse
import com.zavsmit.jokes.data.network.models.JokesResponse
import com.zavsmit.jokes.domain.models.UiModelJoke
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class JokesRepository @Inject constructor(
        private val dao: JokesDao,
        private val api: JokesApi,
        private val sharedPrefs: SharedPrefsHelper,
        private val resourceManager: ResourceManager,
        private val connectivityState: ConnectivityState
) {
    companion object {
        const val ITEM_PER_PAGE = 20L
    }

    fun getRandomJoke(): Observable<List<UiModelJoke>> = dao.getRandomJoke().flatMap { Observable.just(mapToUiList(it)) }

    fun getJokes(): Observable<List<UiModelJoke>> {
        return Observable.concatArrayEager(
                dao.getJokes().flatMap {
                    Observable.just(mapToUiList(it))
                },
                Observable.defer {
                    if (connectivityState.isOnline())
                        getApiRequest().subscribeOn(Schedulers.io())
                                .map { mapToDbList(it.value) }
                                .flatMap {
                                    dao.deleteJokes()
                                            .andThen(dao.insertJokes(it))
                                            .andThen(dao.getJokes())
                                }
                                .flatMap { Observable.just(mapToUiList(it)) }
                    else Observable.error(Throwable(resourceManager.getString(R.string.no_internet)))
                }
        )
    }

    fun refreshJokes(): Observable<List<UiModelJoke>> {
        return Observable.defer {
            if (connectivityState.isOnline())
                getApiRequest().subscribeOn(Schedulers.io())
                        .map { mapToDbList(it.value) }
                        .flatMap {
                            dao.deleteJokes()
                                    .andThen(dao.insertJokes(it))
                                    .andThen(dao.getJokes())
                        }
                        .flatMap { Observable.just(mapToUiList(it)) }
            else Observable.error(Throwable(resourceManager.getString(R.string.no_internet)))
        }
    }

    fun getNextPageJokes(pageNumber: Int): Observable<List<UiModelJoke>> {
        val offset = pageNumber * ITEM_PER_PAGE
        return dao.getJokes(offset).flatMap {
            Observable.just(mapToUiList(it))
        }
    }

    fun getMyJokes(): Observable<List<UiModelJoke>> {
        return dao.getMyJokes().flatMap {
            Observable.just(mapMyJokeToUiList(it))
        }
    }

    fun addMyJokeById(id: Long): Completable {
        return Completable.fromAction {
            val jokeDb = dao.getJokeById(id)
            dao.addMyJoke(MyJokeDb(jokeDb.id, jokeDb.joke))
        }
    }

    fun addMyJoke(text: String): Completable {
        return Completable.fromAction { dao.addMyJoke(MyJokeDb(text.hashCode().toLong(), text)) }
    }

    fun deleteMyJoke(id: Long): Completable {
        return dao.deleteMyJoke(id)
    }

    fun getResources() = resourceManager

    private fun getApiRequest(): Observable<JokesResponse> {
        val first = sharedPrefs.getFirstName()
        val last = sharedPrefs.getLastName()

        return if (first.isBlank() && last.isBlank()) {
            api.getJokesList()
        } else {
            val firstName = if (first.isBlank()) resourceManager.getString(R.string.chuck) else first
            val lastName = if (last.isBlank()) resourceManager.getString(R.string.norris) else last
            api.getJokesListWithCustomName(firstName, lastName)
        }
    }


    private fun checkOnMyJoke(listDb: List<JokeDb>): List<Long> {
        val listIds = mutableListOf<Long>().apply {
            listDb.forEach { add(it.id) }
        }
        return dao.getMyJokesIdsByIds(listIds)
    }


    private fun mapToUiList(list: List<JokeDb>): List<UiModelJoke> {
        val myJokeIdsList = checkOnMyJoke(list)
        return mutableListOf<UiModelJoke>().apply {
            list.forEach {
                val textButton = if (myJokeIdsList.contains(it.id))
                    resourceManager.getString(R.string.dislike)
                else resourceManager.getString(R.string.like)

                add(UiModelJoke(id = it.id, text = it.joke, likeButtonText = textButton))
            }
        }
    }

    private fun mapMyJokeToUiList(list: List<MyJokeDb>): List<UiModelJoke> {
        val first = sharedPrefs.getFirstName()
        val last = sharedPrefs.getLastName()
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

    private fun mapToDbList(list: List<JokeItemResponse>): List<JokeDb> {
        return mutableListOf<JokeDb>().apply {
            list.forEach { add(JokeDb(it.id, it.joke)) }
        }
    }
}
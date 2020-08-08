package com.zavsmit.jokes.data

import com.zavsmit.jokes.core.ConnectivityState
import com.zavsmit.jokes.data.db.JokesDao
import com.zavsmit.jokes.data.db.models.JokeDb
import com.zavsmit.jokes.data.network.JokesApi
import com.zavsmit.jokes.data.network.models.JokeItemResponse
import com.zavsmit.jokes.ui.jokes.UiModelJoke
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class JokesRepository @Inject constructor(
        private val dao: JokesDao,
        private val api: JokesApi,
        private val sharedPrefs: SharedPrefsHelper,
        private val connectivityState: ConnectivityState
) {

    //получить данные из базы
    // отдать на отрисовку
    // получить из сети
    // поменять базу
    // обновить данные в ui

    fun getJokes(): Observable<List<UiModelJoke>> {
        return Observable.concatArrayEager(
                dao.getJokes(0, 20).flatMap {
                    Observable.just(mapToUiList(it))
                }.subscribeOn(Schedulers.io()),
                Observable.defer {
                    if (connectivityState.isOnline())
                        getApiRequest().subscribeOn(Schedulers.io())
                                .map { mapToDbList(it.value) }
                                .flatMap { dao.deleteJokes().andThen(dao.insertJokes(it)).andThen(dao.getJokes(0,20)) }
                                .flatMap { Observable.just(mapToUiList(it)) }
                    else Observable.empty()
                }.subscribeOn(Schedulers.io())
        )
    }


    private fun getApiRequest() = if (sharedPrefs.getFirstName().isBlank() && sharedPrefs.getLastName().isBlank()) {
        api.getJokesList()
    } else {
        api.getJokesListWithCustomName(sharedPrefs.getFirstName(), sharedPrefs.getLastName())
    }

//     fun getMoreJokes(
//            arg: GetMoreJokesUseCase.MoreJokesArgsModel
//    ): Single<List<JokeDomainModel>> {
//        return dao.getNextJokes(arg.lastMaxId, arg.requestedLoadSize).map { list ->
//            mapToDomainList(list)
//        }
//    }

//     fun getRandomJoke(): Single<JokeDomainModel> {
//        return dao.getRandomJoke().map { it.mapToDomain() }
//    }
//
//     fun addMyJoke(text: String): Completable {
//        return dao.getMyJokesMaxId()
//                .defaultIfEmpty(0)
//                .map { it }
//                .flatMapCompletable {
//                    dao.addMyJoke(MyJokeDbEntity(it + 1, text))
//                }
//    }

//     fun deleteJoke(id: Long): Completable {
//        return dao.deleteJoke(id)
//    }
//
//     fun getMyJokes(): Single<List<JokeDomainModel>> {
//        return dao.getMyJokes().map { list ->
//            mutableListOf<JokeDomainModel>().apply {
//                list.forEach {
//                    add(it.mapToDomain())
//                }
//            }.toList()
//        }
//    }


    private fun mapToUiList(list: List<JokeDb>): List<UiModelJoke> {
        return mutableListOf<UiModelJoke>().apply {
            list.forEach {
                add(UiModelJoke(id = it.id, text = it.joke))
            }
        }.toList()
    }

    private fun mapToUiList1(list: List<JokeDb>): Observable<List<UiModelJoke>> {
        return Observable.fromArray(mutableListOf<UiModelJoke>().apply {
            list.forEach {
                add(UiModelJoke(id = it.id, text = it.joke))
            }
        }.toList())
    }

    private fun mapToDbList(list: List<JokeItemResponse>): List<JokeDb> {
        return mutableListOf<JokeDb>().apply {
            list.forEach { add(JokeDb(it.id, it.joke)) }
        }.toList()
    }

}
package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.NoArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import com.zavsmit.jokes.data.db.models.MyJokeDb
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetMyJokesUseCase @Inject constructor(private val repository: JokesRepository) :
        NoArgsUseCase<Observable<List<MyJokeDb>>> {

    override fun execute(): Observable<List<MyJokeDb>> {
        return repository.getMyJokes()
    }
}
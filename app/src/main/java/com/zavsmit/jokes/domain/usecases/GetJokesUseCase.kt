package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.NoArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import com.zavsmit.jokes.data.db.models.JokeDb
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetJokesUseCase @Inject constructor(private val repository: JokesRepository) :
        NoArgsUseCase<Observable<List<JokeDb>>> {

    override fun execute(): Observable<List<JokeDb>> {
        return repository.getJokes()
    }
}
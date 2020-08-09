package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.NoArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import com.zavsmit.jokes.data.db.models.JokeDb
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetRandomJokeUseCase @Inject constructor(private val repository: JokesRepository) :
    NoArgsUseCase<Single<List<JokeDb>>> {

    override fun execute(): Single<List<JokeDb>> {
        return repository.getRandomJoke()
    }
}
package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.ArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import com.zavsmit.jokes.data.db.models.JokeDb
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetNextJokesUseCase @Inject constructor(private val repository: JokesRepository) :
        ArgsUseCase<GetNextJokesUseCase.NextJokesArgsModel, Observable<List<JokeDb>>> {

    override fun execute(args: NextJokesArgsModel): Observable<List<JokeDb>> {
        return repository.getNextPageJokes(args.pageNumber)
    }

    data class NextJokesArgsModel(val pageNumber: Int)
}
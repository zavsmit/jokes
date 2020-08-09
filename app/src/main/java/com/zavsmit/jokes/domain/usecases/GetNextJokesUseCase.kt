package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.ArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import com.zavsmit.jokes.domain.models.UiModelJoke
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetNextJokesUseCase @Inject constructor(private val repository: JokesRepository) :
        ArgsUseCase<GetNextJokesUseCase.NextJokesArgsModel, Observable<List<UiModelJoke>>> {

    override fun execute(args: NextJokesArgsModel): Observable<List<UiModelJoke>> {
        return repository.getNextPageJokes(args.pageNumber)
    }

    data class NextJokesArgsModel(val pageNumber: Int)
}
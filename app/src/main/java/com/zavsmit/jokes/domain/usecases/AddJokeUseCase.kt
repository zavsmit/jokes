package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.CompletableArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject


class AddJokeUseCase @Inject constructor(private val repository: JokesRepository) : CompletableArgsUseCase<AddJokeUseCase.Arg> {

    override fun execute(args: Arg): Completable {
        return repository.addMyJoke(args.text)
    }

    data class Arg(val text: String)
}
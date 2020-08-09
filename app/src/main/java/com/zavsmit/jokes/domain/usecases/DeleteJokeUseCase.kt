package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.CompletableArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteJokeUseCase @Inject constructor(private val repository: JokesRepository) :
        CompletableArgsUseCase<DeleteJokeUseCase.Arg> {

    override fun execute(args: Arg): Completable {
        return repository.deleteMyJoke(args.id)
    }

    data class Arg(val id: Long)
}
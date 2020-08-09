package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.ArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import com.zavsmit.jokes.data.db.models.JokeDb
import javax.inject.Inject


class GetMyJokeIdsUseCase @Inject constructor(private val repository: JokesRepository) :
        ArgsUseCase<GetMyJokeIdsUseCase.Arg, List<Long>> {

    override fun execute(args: Arg): List<Long> {
        return repository.checkOnMyJoke(args.listDb)
    }

    data class Arg(val listDb: List<JokeDb>)
}
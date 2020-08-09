package com.zavsmit.jokes.domain.usecases

import com.zavsmit.jokes.core.NoArgsUseCase
import com.zavsmit.jokes.data.JokesRepository
import com.zavsmit.jokes.domain.models.UiModelJoke
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetJokesUseCase @Inject constructor(private val repository: JokesRepository) :
        NoArgsUseCase<Observable<List<UiModelJoke>>> {

    override fun execute(): Observable<List<UiModelJoke>> {
        return repository.getJokes()
    }
}
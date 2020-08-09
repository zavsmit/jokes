package com.zavsmit.jokes.core

import io.reactivex.rxjava3.core.Completable

interface ArgsUseCase<T, R> : UseCase {
    fun execute(args: T): R
}

interface NoArgsUseCase<R> : UseCase {
    fun execute(): R
}

interface CompletableArgsUseCase<T> : UseCase {
    fun execute(args: T) : Completable
}

interface UseCase
package com.audcode.domain.interactor

import io.reactivex.Flowable
import io.reactivex.Single

interface UseCase<P,R> {
    fun execute(param:P): Flowable<R>
}
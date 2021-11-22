package com.turtle.yososuwhere.domain.usecases.common


import io.reactivex.Completable
import io.reactivex.Scheduler

abstract class CompletableUseCase<in Params>(
    subscribeScheduler: Scheduler,
    observeScheduler: Scheduler
) : BaseReactiveUseCase(subscribeScheduler, observeScheduler) {

    abstract fun buildUseCaseCompletable(params: Params? = null): Completable

    fun execute(params: Params? = null) : Completable{
        return buildUseCaseCompletable(params)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }
}

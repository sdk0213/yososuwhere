package com.turtle.yososuwhere.domain.usecases.common

import io.reactivex.Observable
import io.reactivex.Scheduler

abstract class ObservableUseCase<Results, Params>
    (
    subscribeScheduler: Scheduler,
    observeScheduler: Scheduler
) : BaseReactiveUseCase(subscribeScheduler, observeScheduler) {

    abstract fun buildUseCaseCompletable(params: Params? = null): Observable<Results>

    fun execute(params: Params? = null): Observable<Results> {
        return buildUseCaseCompletable(params)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }


}
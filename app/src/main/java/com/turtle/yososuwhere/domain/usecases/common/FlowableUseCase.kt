package com.turtle.yososuwhere.domain.usecases.common

import io.reactivex.Flowable
import io.reactivex.Scheduler

abstract class FlowableUseCase<Results, Params>
    (
    subscribeScheduler: Scheduler,
    observeScheduler: Scheduler
) : BaseReactiveUseCase(subscribeScheduler, observeScheduler){

    abstract fun buildUseCaseCompletable(params: Params? = null): Flowable<Results>

    fun execute(params: Params? = null) : Flowable<Results> {
        return buildUseCaseCompletable(params)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }


}
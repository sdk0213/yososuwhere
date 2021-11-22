package com.turtle.yososuwhere.domain.usecases.common

import io.reactivex.Scheduler
import io.reactivex.Single

abstract class SingleUseCase<Results, Params>
    (
    subscribeScheduler: Scheduler,
    observeScheduler: Scheduler
) : BaseReactiveUseCase(subscribeScheduler, observeScheduler){

    abstract fun buildUseCaseCompletable(params: Params? = null): Single<Results>

    fun execute(params: Params? = null) : Single<Results> {
        return buildUseCaseCompletable(params)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }


}
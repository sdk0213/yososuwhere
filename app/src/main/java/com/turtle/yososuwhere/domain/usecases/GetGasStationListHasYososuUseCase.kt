package com.turtle.yososuwhere.domain.usecases

import com.turtle.yososuwhere.domain.model.YososuAndLoadPage
import com.turtle.yososuwhere.domain.repository.YososuRepository
import com.turtle.yososuwhere.domain.usecases.common.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetGasStationListHasYososuUseCase @Inject constructor(private val repository: YososuRepository) :
    FlowableUseCase<YososuAndLoadPage, Nothing>(Schedulers.io(), AndroidSchedulers.mainThread()) {

    override fun buildUseCaseCompletable(params: Nothing?): Flowable<YososuAndLoadPage> {
        return repository.getGasStationListHasYososuByPagingConcat(1)
    }
}
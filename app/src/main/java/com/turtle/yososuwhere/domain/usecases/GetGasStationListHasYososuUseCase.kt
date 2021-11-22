package com.turtle.yososuwhere.domain.usecases

import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.repository.YososuRepository
import com.turtle.yososuwhere.domain.usecases.common.SingleUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetGasStationListHasYososuUseCase @Inject constructor(private val repository: YososuRepository) :
    SingleUseCase<List<YososuStation>, Nothing>(Schedulers.io(), AndroidSchedulers.mainThread()) {

    override fun buildUseCaseCompletable(params: Nothing?): Single<List<YososuStation>> {
        return repository.getGasStationListHasYososu()
    }
}
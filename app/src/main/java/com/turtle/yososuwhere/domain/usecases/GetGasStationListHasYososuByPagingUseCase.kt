package com.turtle.yososuwhere.domain.usecases

import androidx.paging.PagingData
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.repository.YososuRepository
import com.turtle.yososuwhere.domain.usecases.common.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetGasStationListHasYososuByPagingUseCase @Inject constructor(private val repository: YososuRepository) :
    FlowableUseCase<PagingData<YososuStation>, Nothing>(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    ) {

    override fun buildUseCaseCompletable(params: Nothing?): Flowable<PagingData<YososuStation>> {
        return repository.getGasStationListHasYososuByPaging()
    }

}
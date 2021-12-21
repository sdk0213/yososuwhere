package com.turtle.yososuwhere.domain.repository

import androidx.paging.PagingData
import com.turtle.yososuwhere.domain.model.YososuStation
import io.reactivex.Flowable
import io.reactivex.Single

interface YososuRepository {
    fun getGasStationListHasYososuByPaging(): Flowable<PagingData<YososuStation>>
    fun getGasStationListHasYososu(): Single<List<YososuStation>>
}
package com.turtle.yososuwhere.domain.repository

import com.turtle.yososuwhere.domain.model.YososuStation
import io.reactivex.Single

interface YososuRepository {
    fun getGasStationListHasYososu(): Single<List<YososuStation>>
}
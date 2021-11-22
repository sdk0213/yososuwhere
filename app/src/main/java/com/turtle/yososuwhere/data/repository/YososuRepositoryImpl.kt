package com.turtle.yososuwhere.data.repository

import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.repository.YososuRepository
import io.reactivex.Single
import java.lang.Exception
import javax.inject.Inject

class YososuRepositoryImpl @Inject constructor(): YososuRepository {

    override fun getGasStationListHasYososu(): Single<List<YososuStation>> {
        return Single.create {
            it.onError(Exception("d"))
        }
    }

}
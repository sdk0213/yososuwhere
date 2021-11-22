package com.turtle.yososuwhere.data.repository

import com.turtle.yososuwhere.data.api.YososuAPIService
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.repository.YososuRepository
import io.reactivex.Single
import javax.inject.Inject

class YososuRepositoryImpl @Inject constructor(
    private val apiService: YososuAPIService,
): YososuRepository {

    override fun getGasStationListHasYososu(): Single<List<YososuStation>> {
        return apiService.getYososuStationList()
            .flatMap { response ->
                Single.create { emitter ->
                    if (response.isSuccessful) {
                        response.body()?.data?.let { list ->
                            emitter.onSuccess(list)
                        } ?: run {
                            emitter.onError(Exception("noData"))
                        }
                    } else {
                        emitter.onError(Exception(response.errorBody().toString()))
                    }
                }
            }
    }

}
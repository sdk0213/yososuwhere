package com.turtle.yososuwhere.data.repository.yososu

import com.turtle.yososuwhere.data.api.YososuAPIService
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.repository.YososuRepository
import io.reactivex.Single
import javax.inject.Inject

class YososuRepositoryImpl @Inject constructor(
    private val apiService: YososuAPIService,
) : YososuRepository {

    override fun getGasStationListHasYososu(): Single<List<YososuStation>> {
        return apiService.getYososuStationList()
            .flatMap { response ->
                Single.create { emitter ->
                    if (response.isSuccessful) {
                        response.body()?.data?.let { list ->
                            emitter.onSuccess(list
                                .map { entity ->
                                    YososuStation(
                                        code = entity.code ?: "알수 없음",
                                        cost = entity.price ?: "0",
                                        name = entity.name ?: "알수 없음",
                                        lon = entity.lng?.toDouble() ?: 0.0,
                                        lat = entity.lat?.toDouble() ?: 0.0,
                                        dataStandard = entity.regDt ?: "알수 없음",
                                        operationTime = entity.openTime ?: "알수 없음",
                                        stock = entity.inventory?.replace(",", "")?.toLong() ?: 0L,
                                        tel = entity.tel ?: "알수 없음",
                                        addr = entity.addr ?: "알수 없음",
                                        color = entity.color ?: "알수 없음"
                                    )
                                }
                            )
                        } ?: run {
                            emitter.onError(Exception("데이터를 불러오는중 에러가 발생하였습니다."))
                        }
                    } else {
                        emitter.onError(Exception(response.errorBody().toString()))
                    }
                }
            }
    }

}
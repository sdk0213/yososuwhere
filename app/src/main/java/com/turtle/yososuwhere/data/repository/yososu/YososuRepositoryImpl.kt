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
                                        code = entity.코드,
                                        cost = entity.가격,
                                        name = entity.명칭,
                                        lon = entity.경도.toDouble(),
                                        lat = entity.위도.toDouble(),
                                        dataStandard = entity.데이터기준일,
                                        operationTime = entity.영업시간,
                                        stock = entity.재고량.replace(",","").toLong(),
                                        tel = entity.전화번호,
                                        addr = entity.주소
                                    )
                                }
                            )
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
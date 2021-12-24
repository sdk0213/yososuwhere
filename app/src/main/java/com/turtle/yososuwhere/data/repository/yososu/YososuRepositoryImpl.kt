package com.turtle.yososuwhere.data.repository.yososu

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.turtle.yososuwhere.data.api.YososuAPIService
import com.turtle.yososuwhere.domain.model.LoadPage
import com.turtle.yososuwhere.domain.model.YososuAndLoadPage
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.repository.YososuRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class YososuRepositoryImpl @Inject constructor(
    private val remoteDataSource: YososuRemoteDataSource,
    private val apiService: YososuAPIService
) : YososuRepository {

    // code from: https://stackoverflow.com/questions/28047272/handle-paging-with-rxjava
    // todo: Mapper 사용
    override fun getGasStationListHasYososuByPagingConcat(page: Int): Flowable<YososuAndLoadPage> {
        val perPage = 400
        return apiService.getYososuStationList(page, perPage)
            .toFlowable()
            .concatMap { response ->
                if (response.isSuccessful) {
                    val totalPage = (response.body()!!.totalCount / perPage) + 1
                    val nowPage = response.body()!!.page
                    // 마지막 페이지
                    if (totalPage == nowPage) {
                        Flowable.just(
                            YososuAndLoadPage(
                                yososuStations = response.body()?.let { body ->
                                    body.data.map { entity ->
                                        YososuStation(
                                            code = entity.code ?: "알수 없음",
                                            cost = entity.price ?: "0",
                                            name = entity.name ?: "알수 없음",
                                            lon = entity.lng?.toDouble() ?: 0.0,
                                            lat = entity.lat?.toDouble() ?: 0.0,
                                            dataStandard = entity.regDt ?: "알수 없음",
                                            operationTime = entity.openTime ?: "알수 없음",
                                            stock = entity.inventory?.replace(",", "")?.toLong()
                                                ?: 0L,
                                            tel = entity.tel ?: "알수 없음",
                                            addr = entity.addr ?: "알수 없음",
                                            color = entity.color ?: "알수 없음"
                                        )
                                    }
                                } ?: run { arrayListOf(YososuStation()) },
                                loadPage = LoadPage(
                                    nowPage = nowPage,
                                    totalPage = totalPage
                                )
                            )
                        )
                    }
                    // 다음 페이지
                    else {
                        Flowable.just(
                            YososuAndLoadPage(
                                yososuStations = response.body()?.let { body ->
                                    body.data.map { entity ->
                                        YososuStation(
                                            code = entity.code ?: "알수 없음",
                                            cost = entity.price ?: "0",
                                            name = entity.name ?: "알수 없음",
                                            lon = entity.lng?.toDouble() ?: 0.0,
                                            lat = entity.lat?.toDouble() ?: 0.0,
                                            dataStandard = entity.regDt ?: "알수 없음",
                                            operationTime = entity.openTime ?: "알수 없음",
                                            stock = entity.inventory?.replace(",", "")?.toLong()
                                                ?: 0L,
                                            tel = entity.tel ?: "알수 없음",
                                            addr = entity.addr ?: "알수 없음",
                                            color = entity.color ?: "알수 없음"
                                        )
                                    }
                                } ?: run { arrayListOf(YososuStation()) },
                                loadPage = LoadPage(
                                    nowPage = nowPage,
                                    totalPage = totalPage
                                )
                            )
                        ).concatWith(getGasStationListHasYososuByPagingConcat(page = nowPage + 1))
                    }
                } else {
                    Flowable.create({ emitter ->
                        emitter.onError(Exception(response.errorBody().toString()))
                    }, BackpressureStrategy.MISSING)

                }
            }
    }

    override fun getGasStationListHasYososuByPaging(): Flowable<PagingData<YososuStation>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 20,
                initialLoadSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = { remoteDataSource }
        ).flowable
    }

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
package com.turtle.yososuwhere.data.repository.yososu

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.turtle.yososuwhere.data.api.YososuAPIService
import com.turtle.yososuwhere.domain.model.YososuStation
import io.reactivex.Single

class YososuRemoteDataSource(
    private val apiService: YososuAPIService,
) : RxPagingSource<Int, YososuStation>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, YososuStation>> {
        val position = params.key ?: 1

        return apiService.getYososuStationList(
            page = position,
            perPage = 10,
        )
            .map { response ->
                if (response.isSuccessful) {
                    LoadResult.Page(
                        data = response.body()!!.data.map { entity ->
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
                        },
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (position == (response.body()!!.totalCount / response.body()!!.perPage) + 1) null else position + 1
                    )
                } else {
                    LoadResult.Error(
                        throwable = Exception(response.errorBody().toString())
                    )
                }

            }
    }

    override fun getRefreshKey(state: PagingState<Int, YososuStation>): Int? {
        return state.anchorPosition
    }

}
package com.turtle.yososuwhere.data.api

import com.turtle.yososuwhere.data.model.YososuStationJsonBody
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URLDecoder

// 공공 데이터 포털
interface YososuAPIService {

    // 요소수 주유소 정보 가져오기
    @GET("inventory?")
    fun getYososuStationList(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 1000,
        @Query("serviceKey") serviceKey: String = URLDecoder.decode(
            ApiClient.YOSOSU_API_KEY,
            "UTF-8"
        ),
    ): Single<Response<YososuStationJsonBody>>

}
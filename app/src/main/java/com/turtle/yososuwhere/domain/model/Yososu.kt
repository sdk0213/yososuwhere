package com.turtle.yososuwhere.domain.model

data class YososuStationJson(
    val response: YososuStationJsonResponse
)

data class YososuStationJsonResponse(
    val header: YososuStationJsonHeader,
    val body: YososuStationJsonBody
)

data class YososuStationJsonHeader(
    val resultCode: Int,
    val resultMsg: String
)

data class YososuStationJsonBody(
    val currentCount: Int,
    val data: YososuStationJsonItems,
    val page: Int,
    val perPage: Int,
    val totalCount: Int
)

data class YososuStationJsonItems(
    val item: List<YososuStation>
)

data class YososuStation(
    val 가격: String,
    val 경도: String,
    val 데이터기준일: String,
    val 명칭: String,
    val 영업시간: String,
    val 위도: String,
    val 재고량: Int,
    val 전화번호: String,
    val 주소: String,
    val 코드: String
)

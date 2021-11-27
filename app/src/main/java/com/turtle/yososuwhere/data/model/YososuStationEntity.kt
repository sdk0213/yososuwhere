package com.turtle.yososuwhere.data.model

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
    val data: List<YososuStationEntity>,
    val page: Int,
    val perPage: Int,
    val totalCount: Int
)

data class YososuStationEntity(
    val price: String?,
    val lng: String?,
    val regDt: String?,
    val name: String?,
    val openTime: String?,
    val lat: String?,
    val inventory: String?,
    val tel: String?,
    val addr: String?,
    val code: String?,
    val color: String?
)
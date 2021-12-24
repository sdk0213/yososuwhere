package com.turtle.yososuwhere.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YososuStation(
    val code: String = "0",
    val cost: String = "0",
    val name: String = "0",
    val lon: Double = 0.0,
    val lat: Double = 0.0,
    val dataStandard: String = "0",
    val operationTime: String = "0",
    val stock: Long = 0L,
    val tel: String = "0",
    val addr: String = "0",
    val color: String = "0", // 잔량 수량 구간
    val kilometer: Double = 0.0
) : Parcelable

data class LoadPage(
    val nowPage: Int,
    val totalPage: Int
)

data class YososuAndLoadPage(
    val yososuStations: List<YososuStation>,
    val loadPage: LoadPage
)
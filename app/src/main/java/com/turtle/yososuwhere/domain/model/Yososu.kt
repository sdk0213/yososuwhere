package com.turtle.yososuwhere.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YososuStation(
    val code: String,
    val cost: String,
    val name: String,
    val lon: Double,
    val lat: Double,
    val dataStandard: String,
    val operationTime: String,
    val stock: Long,
    val tel: String,
    val addr: String,
    val color: String, // 잔량 수량 구간
    val kilometer: Double = 0.0
) : Parcelable

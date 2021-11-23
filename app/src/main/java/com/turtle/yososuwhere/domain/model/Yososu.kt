package com.turtle.yososuwhere.domain.model

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
    val addr: String
)

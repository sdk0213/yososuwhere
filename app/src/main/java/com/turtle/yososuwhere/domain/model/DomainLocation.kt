package com.turtle.yososuwhere.domain.model

data class DomainLocation (
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0.0f,
    val address: String
)
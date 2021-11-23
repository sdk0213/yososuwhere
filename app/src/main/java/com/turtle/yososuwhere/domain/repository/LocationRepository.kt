package com.turtle.yososuwhere.domain.repository

import com.turtle.yososuwhere.domain.model.DomainLocation
import io.reactivex.Flowable

interface LocationRepository {
    fun getLocation(): Flowable<DomainLocation>
}
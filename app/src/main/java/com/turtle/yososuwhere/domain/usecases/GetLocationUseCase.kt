package com.turtle.yososuwhere.domain.usecases

import com.turtle.yososuwhere.domain.model.DomainLocation
import com.turtle.yososuwhere.domain.repository.LocationRepository
import com.turtle.yososuwhere.domain.usecases.common.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) : FlowableUseCase<DomainLocation, Nothing>(Schedulers.io(), AndroidSchedulers.mainThread()) {

    override fun buildUseCaseCompletable(params: Nothing?): Flowable<DomainLocation> {
        return repository.getLocation()
    }

}
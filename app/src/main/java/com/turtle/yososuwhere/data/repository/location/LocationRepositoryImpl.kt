package com.turtle.yososuwhere.data.repository.location

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.turtle.yososuwhere.domain.model.DomainLocation
import com.turtle.yososuwhere.domain.repository.LocationRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest,
    private val geocoder: Geocoder
) : LocationRepository {

    private val locationSubject = PublishSubject.create<DomainLocation>()
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.locations?.forEach(::setLocation)
        }
    }

    override fun getLocation(): Flowable<DomainLocation> {
        return locationSubject
            .toFlowable(BackpressureStrategy.MISSING)
            .doOnSubscribe { startLocationUpdates() }
            .doOnCancel { stopLocationUpdates() }
            .doOnComplete { stopLocationUpdates() }
            .doOnError { stopLocationUpdates() }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(::setLocation)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun setLocation(location: Location?) {
        location?.let {
            locationSubject.onNext(
                DomainLocation(
                    location.latitude,
                    location.longitude,
                    location.accuracy,
                    address =
                    try {
                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude, 1
                        )?.let {
                            if (it.size > 0) {
                                it[0]?.getAddressLine(0).toString()
                            } else {
                                "주소를 가져올수 없습니다."
                            }
                        } ?: "주소를 가져올수 없습니다."
                    } catch (e: IOException) {
                        "주소를 가져올수 없습니다."
                    } catch (e: InvocationTargetException) {
                        "주소를 가져올수 없습니다."
                    }
                )
            )
        }
    }

}
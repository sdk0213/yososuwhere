package com.turtle.yososuwhere.presentation.android.di.module

import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.turtle.yososuwhere.data.api.YososuAPIService
import com.turtle.yososuwhere.data.repository.location.LocationRepositoryImpl
import com.turtle.yososuwhere.data.repository.yososu.YososuRemoteDataSource
import com.turtle.yososuwhere.data.repository.yososu.YososuRepositoryImpl
import com.turtle.yososuwhere.domain.repository.LocationRepository
import com.turtle.yososuwhere.domain.repository.YososuRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideYososuRepositoryImpl(
        remoteDataSource: YososuRemoteDataSource,
        apiService: YososuAPIService
    ): YososuRepository {
        return YososuRepositoryImpl(remoteDataSource, apiService)
    }

    @Provides
    @Singleton
    fun provideLocationRepositoryImpl(
        fusedLocationProviderClient: FusedLocationProviderClient,
        locationRequest: LocationRequest,
        geocoder: Geocoder
    ): LocationRepository {
        return LocationRepositoryImpl(
            fusedLocationProviderClient,
            locationRequest,
            geocoder
        )
    }

}
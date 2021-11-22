package com.turtle.yososuwhere.presentation.android.di.module

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.turtle.yososuwhere.presentation.android.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilModule {

    // FusedLocation
    // 위치 가져오기 관련
    @Singleton
    @Provides
    fun provideFusedLocation(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    // LocationRequest
    // 위치 가져오기 관련
    @Singleton
    @Provides
    fun provideLocationRequest(): LocationRequest {
        return LocationRequest
            .create().apply {
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                interval = 0
            }
    }

    // Geocoder
    // 좌표 -> 주소
    @Singleton
    @Provides
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Geocoder(context, context.resources.configuration.locales.get(0))
        } else {
            Geocoder(context, context.resources.configuration.locale)
        }
    }

}
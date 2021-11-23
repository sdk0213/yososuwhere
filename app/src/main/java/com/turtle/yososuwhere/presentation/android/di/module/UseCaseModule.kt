package com.turtle.yososuwhere.presentation.android.di.module

import com.turtle.yososuwhere.domain.repository.LocationRepository
import com.turtle.yososuwhere.domain.repository.YososuRepository
import com.turtle.yososuwhere.domain.usecases.GetGasStationListHasYososuUseCase
import com.turtle.yososuwhere.domain.usecases.GetLocationUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetGasStationListHasYososuUseCase(repository: YososuRepository): GetGasStationListHasYososuUseCase {
        return GetGasStationListHasYososuUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLocationUseCase(repository: LocationRepository): GetLocationUseCase {
        return GetLocationUseCase(repository)
    }
}
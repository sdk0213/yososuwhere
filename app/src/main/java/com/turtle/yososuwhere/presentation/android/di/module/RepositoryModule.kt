package com.turtle.yososuwhere.presentation.android.di.module

import com.turtle.yososuwhere.data.api.YososuAPIService
import com.turtle.yososuwhere.data.repository.YososuRepositoryImpl
import com.turtle.yososuwhere.domain.repository.YososuRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideYososuRepositoryImpl(
        apiService: YososuAPIService
    ): YososuRepository {
        return YososuRepositoryImpl(apiService)
    }

}
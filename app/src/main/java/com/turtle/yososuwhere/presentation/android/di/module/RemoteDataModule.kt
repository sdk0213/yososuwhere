package com.turtle.yososuwhere.presentation.android.di.module

import com.turtle.yososuwhere.data.api.YososuAPIService
import com.turtle.yososuwhere.data.repository.yososu.YososuRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteDataModule {

    @Provides
    @Singleton
    fun provideYososuRemoteDataSource(
        api: YososuAPIService
    ): YososuRemoteDataSource {
        return YososuRemoteDataSource(api)
    }
}
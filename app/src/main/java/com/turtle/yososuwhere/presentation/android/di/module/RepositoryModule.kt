package com.turtle.yososuwhere.presentation.android.di.module

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
    ): YososuRepository {
        return YososuRepositoryImpl()
    }

}
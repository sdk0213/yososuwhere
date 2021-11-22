package com.turtle.yososuwhere.presentation.android.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.turtle.yososuwhere.presentation.android.AndroidUtil
import com.turtle.yososuwhere.presentation.android.App
import com.turtle.yososuwhere.presentation.android.di.qualifier.ApplicationContext
import com.turtle.yososuwhere.presentation.utilities.CatchUnexpectedException
import com.turtle.yososuwhere.presentation.utilities.CustomTimberDebug
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideApp(app: App): Application = app

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(app: App): Context = app

    @Provides
    fun provideCustomTimberDebug(): CustomTimberDebug {
        return CustomTimberDebug()
    }

    // 예상치 못한 에러 체크
    @Provides
    fun provideCatchException(
        @ApplicationContext context: Context,
        androidUtil: AndroidUtil
    ): CatchUnexpectedException {
        return CatchUnexpectedException(context, androidUtil)
    }

    // 안드로이드 관련 Util
    @Provides
    @Singleton
    fun provideAndroidUtil(@ApplicationContext context: Context): AndroidUtil {
        return AndroidUtil(context)
    }

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("YOSOSUWHERE", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

}
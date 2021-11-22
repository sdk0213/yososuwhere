package com.turtle.yososuwhere.presentation.android

import com.turtle.yososuwhere.BuildConfig
import com.turtle.yososuwhere.presentation.android.di.DaggerAppComponent
import com.turtle.yososuwhere.presentation.utilities.CatchUnexpectedException
import com.turtle.yososuwhere.presentation.utilities.CustomTimberDebug
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class App : DaggerApplication() {

    @Inject
    lateinit var customTimberDebug: CustomTimberDebug

    @Inject
    lateinit var catchUnexpectedException: CatchUnexpectedException

    override fun onCreate() {
        super.onCreate()

        // Timber 로그 라이브러리 초기화
        if (BuildConfig.DEBUG) {
            Timber.plant(customTimberDebug)
            Timber.d("onCreate")
        }

        Thread.setDefaultUncaughtExceptionHandler(catchUnexpectedException)

    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication?> {
        return DaggerAppComponent.factory().create(this)
    }

}
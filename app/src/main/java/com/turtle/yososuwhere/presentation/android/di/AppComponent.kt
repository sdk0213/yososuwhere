package com.turtle.yososuwhere.presentation.android.di

import com.turtle.yososuwhere.presentation.android.App
import com.turtle.yososuwhere.presentation.android.di.module.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AndroidActivityModule::class,  // 액티비티 스코프 모듈
        AppModule::class,
        NetModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        UtilModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>
}
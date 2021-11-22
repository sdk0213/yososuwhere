package com.turtle.yososuwhere.presentation.android.di.module

import com.turtle.yososuwhere.presentation.android.di.scope.ActivityScope
import com.turtle.yososuwhere.presentation.view.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [ViewModule::class])
    abstract fun mainActivity(): MainActivity
}
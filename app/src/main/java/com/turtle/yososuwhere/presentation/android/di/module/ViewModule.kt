package com.turtle.yososuwhere.presentation.android.di.module

import android.content.Context
import androidx.databinding.DataBindingUtil
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.ActivityMainBinding
import com.turtle.yososuwhere.presentation.android.di.qualifier.ActivityContext
import com.turtle.yososuwhere.presentation.android.di.scope.ActivityScope
import com.turtle.yososuwhere.presentation.android.di.scope.FragmentScope
import com.turtle.yososuwhere.presentation.view.main.MainActivity
import com.turtle.yososuwhere.presentation.view.main.MainFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewModule {

    companion object {

        @Provides
        @ActivityScope
        fun provideBinding(activity: MainActivity): ActivityMainBinding {
            return DataBindingUtil.setContentView(activity, R.layout.activity_main)
        }

        @Provides
        @ActivityContext
        fun provideContext(activity: MainActivity): Context {
            return activity
        }

    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun getMainFragment(): MainFragment

}
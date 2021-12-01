package com.turtle.yososuwhere.presentation.android.di.module

import android.content.Context
import androidx.databinding.DataBindingUtil
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.ActivityMainBinding
import com.turtle.yososuwhere.presentation.android.di.qualifier.ActivityContext
import com.turtle.yososuwhere.presentation.android.di.scope.ActivityScope
import com.turtle.yososuwhere.presentation.android.di.scope.FragmentScope
import com.turtle.yososuwhere.presentation.view.dialog.TextViewDialogFragment
import com.turtle.yososuwhere.presentation.view.home.HomeFragment
import com.turtle.yososuwhere.presentation.view.main.MainActivity
import com.turtle.yososuwhere.presentation.view.qna.QNAFragment
import com.turtle.yososuwhere.presentation.view.yososu_map.YososuMapFragment
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
    abstract fun getMainFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [QNAModule::class])
    abstract fun getQNAFragment(): QNAFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [MapModule::class])
    abstract fun getMapFragment(): YososuMapFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [TextViewValueModule::class])
    abstract fun getTextViewDialogFragment(): TextViewDialogFragment

}
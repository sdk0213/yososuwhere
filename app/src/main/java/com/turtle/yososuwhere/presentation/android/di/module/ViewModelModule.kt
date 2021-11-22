package com.turtle.yososuwhere.presentation.android.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.turtle.yososuwhere.presentation.android.di.factory.ViewModelFactory
import com.turtle.yososuwhere.presentation.android.di.key.ViewModelKey
import com.turtle.yososuwhere.presentation.view.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(viewModel: MainViewModel): ViewModel

}
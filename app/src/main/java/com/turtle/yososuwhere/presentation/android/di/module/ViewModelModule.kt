package com.turtle.yososuwhere.presentation.android.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.turtle.yososuwhere.presentation.android.di.factory.ViewModelFactory
import com.turtle.yososuwhere.presentation.android.di.key.ViewModelKey
import com.turtle.yososuwhere.presentation.view.home.HomeViewModel
import com.turtle.yososuwhere.presentation.view.qna.QNAViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsMainViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QNAViewModel::class)
    abstract fun bindsQNAViewModel(viewModel: QNAViewModel): ViewModel
}
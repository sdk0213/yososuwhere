package com.turtle.yososuwhere.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.usecases.GetGasStationListHasYososuByPagingUseCase
import com.turtle.yososuwhere.presentation.view.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getGasStationsByPagingUseCase: GetGasStationListHasYososuByPagingUseCase
) : BaseViewModel() {

    private val _yososuStationPagingData = MutableLiveData<PagingData<YososuStation>>()
    val yososuStationPagingData: LiveData<PagingData<YososuStation>> get() = _yososuStationPagingData

    @ExperimentalCoroutinesApi
    fun getYososuStationByPaging() {
        compositeDisposable.add(
            getGasStationsByPagingUseCase.execute()
                .cachedIn(viewModelScope)
                .subscribe(
                    { yososuPagingData ->
                        _yososuStationPagingData.value = yososuPagingData
                    },
                    { throwable ->
                        Timber.e(throwable)
                    }
                )
        )
    }

}
package com.turtle.yososuwhere.presentation.view.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.turtle.yososuwhere.domain.model.DomainLocation
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

    private val _myLocation = MutableLiveData<Location>()
    val myLocation: LiveData<Location> get() = _myLocation

    private val _currentLocation = MutableLiveData(DomainLocation(address = "주소를 가져오는 중입니다."))
    val currentLocation: LiveData<DomainLocation> get() = _currentLocation

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
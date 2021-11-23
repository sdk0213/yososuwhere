package com.turtle.yososuwhere.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.usecases.GetGasStationListHasYososuUseCase
import com.turtle.yososuwhere.presentation.utilities.Event
import com.turtle.yososuwhere.presentation.view.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getGasStationListHasYososuUseCase: GetGasStationListHasYososuUseCase
) : BaseViewModel() {

    private val _yososuStationList = MutableLiveData<List<YososuStation>>()
    val yososuStationEntityList: LiveData<List<YososuStation>> get() = _yososuStationList

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = _errorMessage

    init {
        getYososuStation()
    }

    fun getYososuStation() {
        compositeDisposable.add(
            getGasStationListHasYososuUseCase.execute()
                .subscribe(
                    { yososuList ->
                        val list = yososuList.toMutableList()
                        list.sortBy { it.stock }
                        _yososuStationList.value = list
                    },
                    { throwable ->
                        Timber.e(throwable)
                        _errorMessage.value = Event(throwable.message.toString())
                    }
                )
        )
    }
}
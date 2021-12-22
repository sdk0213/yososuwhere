package com.turtle.yososuwhere.presentation.view.yososu_map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.usecases.GetGasStationListHasYososuUseCase
import com.turtle.yososuwhere.presentation.utilities.Event
import com.turtle.yososuwhere.presentation.view.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class YososuMapViewModel @Inject constructor(
    private val getGasStationListHasYososuUseCase: GetGasStationListHasYososuUseCase
) : BaseViewModel() {

    private val _yososuStationList = MutableLiveData<Event<List<YososuStation>>>()
    val yososuStationList: LiveData<Event<List<YososuStation>>> get() = _yososuStationList

    private val _myLocation = MutableLiveData<Location>()
    val myLocation: LiveData<Location> get() = _myLocation

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = _errorMessage

    fun currentLocation(location: Location) {
        _myLocation.value = location
    }

    fun getYososuStation() {
        compositeDisposable.add(
            getGasStationListHasYososuUseCase.execute()
                .subscribe(
                    { yososuList ->
                        _yososuStationList.value = Event(yososuList)
                    },
                    { throwable ->
                        Timber.e(throwable)
                        _errorMessage.value = Event(throwable.message.toString())
                    }
                )
        )
    }

    fun countStationColor(): String {
        var green = 0
        var yellow = 0
        var red = 0
        var grey = 0
        _yososuStationList.value?.peekContent()?.forEach {
            when (it.color) {
                "GREEN" -> green++
                "YELLOW" -> yellow++
                "RED" -> red++
                else -> grey++
            }
        }
        return "$green/$yellow/$red/$grey"
    }

}
package com.turtle.yososuwhere.presentation.view.yososu_map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.usecases.GetGasStationListHasYososuUseCase
import com.turtle.yososuwhere.presentation.utilities.Event
import com.turtle.yososuwhere.presentation.view.base.BaseViewModel
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
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

    private val _loadingPercent = MutableLiveData<Event<Int>>()
    val loadingPercent: LiveData<Event<Int>> get() = _loadingPercent

    private val totalYososuStations = mutableListOf<YososuStation>()

    fun currentLocation(location: Location) {
        _myLocation.value = location
    }

    fun getYososuStation() {
        clearCountStationColor()
        compositeDisposable.add(
            getGasStationListHasYososuUseCase.execute()
                .subscribe(
                    { yososuAndPage ->
                        _yososuStationList.value = Event(yososuAndPage.yososuStations)
                        totalYososuStations.addAll(yososuAndPage.yososuStations)

                        val nowPage = yososuAndPage.loadPage.nowPage.toDouble()
                        val totalPage = yososuAndPage.loadPage.totalPage.toDouble()
                        val percent = ((nowPage / totalPage) * 100.0).toInt()
                        _loadingPercent.value = Event(percent)
                    },
                    { throwable ->
                        Timber.e(throwable)
                        when (throwable) {
                            is HttpException -> {
                                _errorMessage.value = Event("연결 오류")
                            }
                            is UnknownHostException -> {
                                _errorMessage.value = Event("인터넷 연결을 확인해주세요")
                            }
                            else -> {
                                _errorMessage.value =
                                    Event("알수 없는 오류 입니다.\n${throwable.message.toString()}")
                            }
                        }
                    }
                )
        )
    }

    private fun clearCountStationColor() = totalYososuStations.clear()

    fun countStationColor(): String {
        var green = 0
        var yellow = 0
        var red = 0
        var grey = 0
        totalYososuStations.forEach {
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
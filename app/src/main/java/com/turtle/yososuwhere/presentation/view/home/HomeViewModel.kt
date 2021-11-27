package com.turtle.yososuwhere.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turtle.yososuwhere.domain.model.DomainLocation
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.domain.usecases.GetGasStationListHasYososuUseCase
import com.turtle.yososuwhere.domain.usecases.GetLocationUseCase
import com.turtle.yososuwhere.presentation.utilities.Event
import com.turtle.yososuwhere.presentation.view.base.BaseViewModel
import io.reactivex.Flowable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class HomeViewModel @Inject constructor(
    private val getGasStationListHasYososuUseCase: GetGasStationListHasYososuUseCase,
    private val getLocationUseCase: GetLocationUseCase
) : BaseViewModel() {

    private val _yososuStationList = MutableLiveData<Event<List<YososuStation>>>()
    val yososuStationEntityList: LiveData<Event<List<YososuStation>>> get() = _yososuStationList

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = _errorMessage

    private val _currentLocation = MutableLiveData(DomainLocation(address = "주소를 가져오는 중입니다."))
    val currentLocation: LiveData<DomainLocation> get() = _currentLocation

    private val _cannotGetLocation = MutableLiveData<Event<Boolean>>()
    val cannotGetLocation: LiveData<Event<Boolean>> get() = _cannotGetLocation

    fun getYososuStation() {
        compositeDisposable.add(
            getGasStationListHasYososuUseCase.execute()
                .subscribe(
                    { yososuList ->
                        val list = arrayListOf<YososuStation>()
                        var hasLocation = false
                        compositeDisposable.add(getLocationUseCase.execute()
                            .take(1)
                            .timeout(3, TimeUnit.SECONDS, Flowable.empty())
                            .doOnComplete {
                                if (!hasLocation) {
                                    _yososuStationList.postValue(Event(yososuList.sortedByDescending { it.stock }))
                                    _cannotGetLocation.postValue(Event(true))
                                }
                            }
                            .subscribe(
                                { location ->
                                    if (location.latitude != 0.0) {
                                        hasLocation = true
                                        _currentLocation.value = location
                                        yososuList.map {
                                            list.add(
                                                YososuStation(
                                                    code = it.code,
                                                    cost = it.cost,
                                                    name = it.name,
                                                    lon = it.lon,
                                                    lat = it.lat,
                                                    dataStandard = it.dataStandard,
                                                    operationTime = it.operationTime,
                                                    stock = it.stock,
                                                    tel = it.tel,
                                                    addr = it.addr,
                                                    color = it.color,
                                                    kilometer = distance(
                                                        location.latitude,
                                                        location.longitude,
                                                        it.lat,
                                                        it.lon,
                                                        "kilometer"
                                                    )
                                                )
                                            )
                                        }
                                        val sortedByKilometer = list.sortedWith(
                                            compareBy({ it.kilometer }, { it.stock })
                                        )
                                        _yososuStationList.value = Event(sortedByKilometer)
                                    }
                                },
                                { throwable ->
                                    _yososuStationList.postValue(Event(yososuList.sortedByDescending { it.stock }))
                                    Timber.e(throwable)
                                }
                            )
                        )
                    },
                    { throwable ->
                        Timber.e(throwable)
                        _errorMessage.value = Event(throwable.message.toString())
                    }
                )
        )
    }

    // 좌표계를 통한 직선 거리 계산
    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        unit: String
    ): Double {

        val theta: Double = lon1 - lon2
        var dist: Double =
            sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(
                deg2rad(theta)
            )

        dist = acos(dist);
        dist = rad2deg(dist);
        dist *= 60 * 1.1515;

        if (unit == "kilometer") {
            dist *= 1.609344;
        } else if (unit == "meter") {
            dist *= 1609.344;
        }

        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return (deg * Math.PI / 180.0)
    }

    private fun rad2deg(rad: Double): Double {
        return (rad * 180 / Math.PI)
    }

}
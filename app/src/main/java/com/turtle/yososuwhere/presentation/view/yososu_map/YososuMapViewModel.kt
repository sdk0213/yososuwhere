package com.turtle.yososuwhere.presentation.view.yososu_map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turtle.yososuwhere.domain.model.YososuStations
import com.turtle.yososuwhere.presentation.view.base.BaseViewModel
import javax.inject.Inject

class YososuMapViewModel @Inject constructor(
) : BaseViewModel() {

    private val _yososuList = MutableLiveData<YososuStations>()
    val yososuList: LiveData<YososuStations> get() = _yososuList

    private val _myLocation = MutableLiveData<Location>()
    val myLocation: LiveData<Location> get() = _myLocation

    fun currentLocation(location: Location) {
        _myLocation.value = location
    }

    fun setYososuList(yososuList: YososuStations) {
        _yososuList.value = yososuList
    }

}
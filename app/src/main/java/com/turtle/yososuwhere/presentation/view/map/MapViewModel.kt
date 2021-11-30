package com.turtle.yososuwhere.presentation.view.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turtle.yososuwhere.presentation.view.base.BaseViewModel
import javax.inject.Inject

class MapViewModel @Inject constructor(
) : BaseViewModel() {

    private val _myLocation = MutableLiveData<Location>()
    val myLocation: LiveData<Location> get() = _myLocation

    fun currentLocation(location: Location) {
        _myLocation.value = location
    }

}
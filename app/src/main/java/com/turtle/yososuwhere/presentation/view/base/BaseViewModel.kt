package com.turtle.yososuwhere.presentation.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.turtle.yososuwhere.presentation.utilities.Event
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun showProgress() {
        _isLoading.value = Event(true)
    }

    fun dismissProgress() {
        _isLoading.value = Event(false)
    }

}
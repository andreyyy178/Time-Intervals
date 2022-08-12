package com.example.timeintervals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private val _interval = MutableLiveData(0)
    val interval: LiveData<Int>
        get() = _interval

    private val _hours = MutableLiveData(0)
    val hours: LiveData<Int>
        get() = _hours

    private val _minutes = MutableLiveData(0)
    val minutes: LiveData<Int>
        get() = _minutes

    fun setInterval(interval: Int){
        _interval.value = interval
    }

    fun setHours(hours: Int){
        _hours.value = hours
    }

    fun setMinutes(minutes: Int){
        _minutes.value = minutes
    }
}
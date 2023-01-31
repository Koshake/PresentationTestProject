package com.example.presentationtestproject.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailsViewModel(count: Int = 0) : ViewModel() {

    private val _liveData = MutableLiveData<Int>()
    private val liveData: LiveData<Int> = _liveData

    init {
        _liveData.postValue(count)
    }

    fun subscribeToLiveData() = liveData

    fun onIncrement() = _liveData.postValue(_liveData.value?.inc())

    fun onDecrement() = _liveData.postValue(_liveData.value?.dec())

    fun setCount(count: Int) = _liveData.postValue(count)
}

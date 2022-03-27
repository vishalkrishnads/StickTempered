package com.vishalds.sticktempered.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Math.round

class TemperedViewModel : ViewModel() {

    private var _defaultBrightness = 0
    val defaultBrightness: Int
        get() = _defaultBrightness

    private var _currentBrightness = 255
    val currentBrightness: Int
        get() = _currentBrightness

    private val _percentage = MutableLiveData<Int>()
    val percentage: LiveData<Int>
        get() = _percentage

    fun setDefaultBrightness(brightness: Int) {
        _defaultBrightness = brightness
    }

    fun setBrightness(brightness: Int, isPercentage: Boolean) {
        if (isPercentage) {
            _currentBrightness = (brightness / 100) * 255
            _percentage.value = brightness
        } else {
            _currentBrightness = brightness
            _percentage.value = round((brightness.toDouble() / 255) * 100).toInt()
        }
    }

}
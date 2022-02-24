package com.example.weatherapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.repository.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepo: WeatherRepo) : ViewModel(){

    var liveDataWeather : MutableLiveData<WeatherResponse> = MutableLiveData()

    /**
     * fetch weather updates from Weather repo
     */
    fun  fetchWeather(lat: Double, long: Double): LiveData<WeatherResponse> {
        liveDataWeather = weatherRepo.fetchWeather(lat, long) as MutableLiveData<WeatherResponse>
        return liveDataWeather
    }

}
package com.example.weatherapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.repository.WeatherRepo

class WeatherViewModel : BaseViewModel(){
    private val weatherRepo: WeatherRepo by lazy { WeatherRepo() }

    var liveDataWeather : MutableLiveData<WeatherResponse> = MutableLiveData()

    /**
     * fetch weather updates from Weather repo
     */
    fun  fetchWeather(lat: Double, long: Double): LiveData<WeatherResponse> {
        liveDataWeather = weatherRepo.fetchWeather(lat, long) as MutableLiveData<WeatherResponse>
        return liveDataWeather
    }

}
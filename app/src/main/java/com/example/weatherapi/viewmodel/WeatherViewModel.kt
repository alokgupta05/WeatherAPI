package com.example.weatherapi.viewmodel

import androidx.lifecycle.LiveData
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.repository.WeatherRepo

class WeatherViewModel(): BaseViewModel(){
    val weatherRepo: WeatherRepo by lazy { WeatherRepo() }

    lateinit var liveDataWeather : LiveData<WeatherResponse>

    fun  fetchWeather(lat: Double, long: Double): LiveData<WeatherResponse> {
        liveDataWeather   = weatherRepo.fetchWeather(lat, long)
        return liveDataWeather
    }

}
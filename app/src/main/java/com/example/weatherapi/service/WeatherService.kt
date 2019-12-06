package com.example.weatherapi.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.location.Location

import com.example.weatherapi.repository.WeatherRepo
import com.example.weatherapi.util.AppCache

class WeatherService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        val weatherRepo = WeatherRepo()
        val location = AppCache.mUserCurrentLocation
        if (location != null) {
            weatherRepo.fetchWeather(location.latitude, location.longitude)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }
}

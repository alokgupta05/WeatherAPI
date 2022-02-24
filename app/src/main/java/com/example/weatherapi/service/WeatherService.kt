package com.example.weatherapi.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.location.Location

import com.example.weatherapi.repository.WeatherRepo
import com.example.weatherapi.util.AppCache
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap
import javax.inject.Inject

/**
 * This is job scheduler class which will fetch the weather updates for the location
 */
class WeatherService @Inject constructor(private val weatherRepo: WeatherRepo): JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        val location = AppCache.mUserCurrentLocation
        if (location != null) {
            weatherRepo.fetchWeather(location.first, location.second)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }
}

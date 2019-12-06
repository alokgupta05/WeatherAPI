package com.example.weatherapi.util

import android.location.Location
import com.example.weatherapi.models.WeatherResponse

object AppCache {

     var mUserCurrentLocation: Location? = null
     var weatherResponse : WeatherResponse = WeatherResponse()


}

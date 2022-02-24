package com.example.weatherapi.util

import android.location.Location
import com.example.weatherapi.models.WeatherResponse

/**
Local cache to fetch location and and response
 */
object AppCache {

     var mUserCurrentLocation: Pair<Double,Double>? = null
     var weatherResponse : WeatherResponse = WeatherResponse()


}

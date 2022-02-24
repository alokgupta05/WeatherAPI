package com.example.weatherapi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.weatherapi.util.AppCache
import com.example.weatherapi.models.QueryPath

import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.util.AppConstant.API_KEY
import com.example.weatherapi.network.WeatherAPI

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    private val weatherAPI: WeatherAPI,
) {


    /**
     * This method will fetch the API information using the API key and lat lon provided
     */
    fun fetchWeather(lat: Double, lon: Double): LiveData<WeatherResponse> {
        val weatherResponseMutableLiveData = MutableLiveData<WeatherResponse>()

        val queryPath = QueryPath(API_KEY,lat,lon)
        if(checkInCache(lat,lon)){
            //Transformation applied to return the location from cache
            return Transformations.map(weatherResponseMutableLiveData, ::getFromCache)
        }
        /**
         * make network call to fetch weather in background thread
         */
        weatherAPI.getResponse("weather", queryPath.lat, queryPath.lon, queryPath.appid)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        AppCache.weatherResponse = response.body()!!
                        weatherResponseMutableLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    weatherResponseMutableLiveData.postValue(null)
                }
            })


        return weatherResponseMutableLiveData
    }

    /**
    get location from Cache
     */
    private fun getFromCache(weatherResponse: WeatherResponse) = AppCache.weatherResponse

    /**
    check in Cache
     */
    fun checkInCache(lat: Double, lon: Double): Boolean{

        val weatherResponse = AppCache.weatherResponse
        weatherResponse.coord?.also {
            if( it.lat == lat && it.lon==lon){
                return true
            }
        }
        return  false
    }
}

package com.example.weatherapi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.weatherapi.util.AppCache
import com.example.weatherapi.models.QueryPath

import com.example.weatherapi.network.RetrofitService
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.util.AppConstant.API_KEY
import com.example.weatherapi.network.WeatherAPI

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepo {

    private val weatherAPI: WeatherAPI

    init {
        weatherAPI = RetrofitService.cteateService(WeatherAPI::class.java)
    }

     fun fetchWeather(lat: Double, lon: Double): LiveData<WeatherResponse> {
        val weatherResponseMutableLiveData = MutableLiveData<WeatherResponse>()

        val queryPath = QueryPath(API_KEY,lat,lon)
         if(checkInCache(lat,lon)){
             //Applying to return the cache location
             return Transformations.map(weatherResponseMutableLiveData, ::getFromCache)
         }

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

    /*
    get location from Cache
     */
    private fun getFromCache(weatherResponse: WeatherResponse) = AppCache.weatherResponse

    /*
    check in Cache
     */
    fun checkInCache(lat: Double, lon: Double): Boolean{

            var weatherResponse = AppCache.weatherResponse
            if(weatherResponse.coord!=null && weatherResponse!!.coord!!.lat == lat && weatherResponse!!.coord!!.lon==lon){
                return true
            }

        return  false
    }
}

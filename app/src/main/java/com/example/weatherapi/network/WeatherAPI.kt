package com.example.weatherapi.network

import com.example.weatherapi.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface WeatherAPI {

    @GET("{name}")
    fun getResponse(
        @Path("name") name: String, @Query("lat") lat: Double,
        @Query("lon") lon: Double, @Query("appid") appid: String
    ): Call<WeatherResponse>

}

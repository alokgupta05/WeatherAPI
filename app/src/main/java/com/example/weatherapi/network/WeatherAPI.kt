package com.example.weatherapi.network

import com.example.weatherapi.models.WeatherResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

internal interface WeatherAPI {

    @GET("{name}")
    fun getResponse(
        @Path("name") name: String, @Query("lat") lat: Double,
        @Query("lon") lon: Double, @Query("appid") appid: String
    ): Call<WeatherResponse>

}

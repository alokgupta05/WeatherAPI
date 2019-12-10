package com.example.weatherapi.network

import com.example.weatherapi.util.AppConstant.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    /**
     * Create retrofit using Gsonconverted as output  and base url as input
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <S> cteateService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }


}

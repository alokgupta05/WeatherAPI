package com.example.weatherapi.di

interface LocationServicable {

    /**
     * Get user location
     *
     * @return Pair of latitude and longitude
     */
    suspend fun getLocation(): Pair<Double, Double>?
}

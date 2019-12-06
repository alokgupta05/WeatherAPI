package com.example.weatherapi.models

import androidx.lifecycle.Transformations

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Coord {

    @SerializedName("lon")
    @Expose
    var lon: Double = 0.toDouble()
    @SerializedName("lat")
    @Expose
    var lat: Double = 0.toDouble()


}

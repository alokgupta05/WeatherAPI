package com.example.weatherapi.location

import androidx.annotation.RequiresPermission
import com.example.weatherapi.di.LocationServicable
import javax.inject.Inject

/**
 * Implementation of [LocationServicable]
 */
class LocationServicableImpl @Inject constructor(
    private val locationManager: LocationManager,
) : LocationServicable {

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override suspend fun getLocation(): Pair<Double, Double>? {
        if (locationManager.initLocationIfPermissionGranted()) {
            locationManager.getLocation()
        }
        return null
    }
}

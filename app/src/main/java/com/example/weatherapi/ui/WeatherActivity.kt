package com.example.weatherapi.ui

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapi.util.AppCache
import com.example.weatherapi.util.Utils
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.example.weatherapi.R
import com.example.weatherapi.util.AppConstant.LOCATION_REFRESH_TIME
import com.example.weatherapi.util.CustomListener
import com.example.weatherapi.util.LocationPermission


class WeatherActivity : AbstractActivity() ,LocationListener ,
    CustomListener {


    val permissions: LocationPermission by lazy {
        LocationPermission()
    }
    lateinit  var model : WeatherViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this)[WeatherViewModel::class.java]

        if(permissions.isLocationPermissionEnabled(this)){
            fetchLocation()
        }else{
            permissions.checkAndRequestPermission(this,this)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        this.permissions!!.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onLocationChanged(location: Location?) {
        AppCache.mUserCurrentLocation = location
        hideProgressDialog()
        showProgresDialog("Fetching weather")
        model.fetchWeather(location!!.latitude,location!!.longitude).observe(this,
            Observer<WeatherResponse> { weatherReponse ->
                hideProgressDialog()
                temperatureView.text = weatherReponse.main!!.temp.toString()
                cityView.text = weatherReponse.name
                if(weatherReponse.weather!=null && weatherReponse.weather!!.isNotEmpty())
                    cloudValueView.text = weatherReponse.weather!!.get(0).description

                Utils.scheduleJob(this@WeatherActivity)
            })

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    @SuppressLint("MissingPermission")
    override fun fetchLocation() {

        var mLocationManager =  getSystemService(LOCATION_SERVICE) as LocationManager
        val location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(location!=null){
            onLocationChanged(location)
        }else {
            showProgresDialog("location fetch")
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                0F, this
            )
        }
    }

}

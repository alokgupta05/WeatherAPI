package com.example.weatherapi.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapi.databinding.ActivityMainBinding
import com.example.weatherapi.di.LocationServicable
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.util.AppCache
import com.example.weatherapi.util.AppConstant.REQUEST_PERMISSIONS_REQUEST_CODE
import com.example.weatherapi.util.Utils
import com.example.weatherapi.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WeatherActivity : AbstractActivity(){


    val weatherViewModel : WeatherViewModel by viewModels()

    @Inject
    lateinit var locationServicable : LocationServicable

    private lateinit var binding :ActivityMainBinding
    var job: Job?=null

    companion object {
        private const val TAG : String = "WeatherActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            job = GlobalScope.launch {
                val location = async {getCurrentLocation()}
                val pair = location.await()
                fetchWeatherFromLocation(pair)
           }
        }
    }

    /**
     *  get new location
     **/
    private suspend fun getCurrentLocation() : Pair<Double,Double>?{
        return locationServicable.getLocation()

    }

    /**
    fetch weather information using the location object and update UI
     **/
    private fun fetchWeatherFromLocation(location : Pair<Double,Double>?){
        location?.apply {
            AppCache.mUserCurrentLocation = location
            hideProgressDialog()
            showProgresDialog("Fetching weather")
             weatherViewModel.fetchWeather(location.first, location.second).observe(this@WeatherActivity,
                { weatherReponse ->
                    updateWeather(weatherReponse)
                })
        }
    }

    /**
        Update weather information on UI
     **/
    private fun updateWeather(weatherReponse : WeatherResponse){
        hideProgressDialog()
        binding.temperatureView.text = (weatherReponse.main?.temp?.minus(273.15)?.toFloat()).toString()
        binding.cityView.text = weatherReponse.name
        weatherReponse.weather.apply {
            binding.cloudValueView.text = weatherReponse.weather?.get(0)?.description
        }
        Utils.scheduleJob(this@WeatherActivity)
    }

    /*
    Check if permissions granted for location
     */
    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    /**
      * Start requesting for location permission
     **/
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    /**
    Request permission dialog displayed
     **/
    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            startLocationPermissionRequest()

        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    /**
     * remove location updates when user moves away from UI
     */
    override fun onStop() {
        super.onStop()
            job?.cancel(null)
    }

}

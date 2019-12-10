package com.example.weatherapi.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapi.util.AppCache
import com.example.weatherapi.util.Utils
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.location.*

class WeatherActivity : AbstractActivity(){

    private lateinit  var model : WeatherViewModel
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val TAG : String = "WeatherActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.weatherapi.R.layout.activity_main)

        model = ViewModelProviders.of(this)[WeatherViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener { taskLocation ->
                if (taskLocation.isSuccessful && taskLocation.result != null) {

                    val location = taskLocation.result
                    AppCache.mUserCurrentLocation = location
                    hideProgressDialog()
                    showProgresDialog("Fetching weather")
                    location?.apply {  model.fetchWeather(location.latitude, location.longitude).observe(this@WeatherActivity,
                        Observer<WeatherResponse> { weatherReponse ->
                            hideProgressDialog()
                            temperatureView.text = (weatherReponse.main?.temp?.minus(273.15)?.toFloat()).toString()
                            cityView.text = weatherReponse.name
                            weatherReponse.weather.apply {
                                cloudValueView.text = weatherReponse.weather?.get(0)?.description
                            }
                            Utils.scheduleJob(this@WeatherActivity)
                        }) }


                } else {
                    Log.e("weatherActivity", "getLastLocation:exception", taskLocation.exception)
                }
            }
    }

    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
                startLocationPermissionRequest()

        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.")
                // Permission granted.
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> getLastLocation()

                else -> {
                    Log.i(TAG, "else block onRequestPermissionsResult ")
                }
            }
        }
    }




}

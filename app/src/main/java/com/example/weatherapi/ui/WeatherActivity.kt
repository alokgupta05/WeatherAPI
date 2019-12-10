package com.example.weatherapi.ui

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.util.AppCache
import com.example.weatherapi.util.AppConstant.REQUEST_CHECK_SETTINGS
import com.example.weatherapi.util.AppConstant.REQUEST_PERMISSIONS_REQUEST_CODE
import com.example.weatherapi.util.Utils
import com.example.weatherapi.viewmodel.WeatherViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_main.*


class WeatherActivity : AbstractActivity(),GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private lateinit  var model : WeatherViewModel
    private val TAG : String = "WeatherActivity"
    private lateinit var mLocationRequest : LocationRequest
    private lateinit var googleApiClient :  GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.weatherapi.R.layout.activity_main)
        model = ViewModelProviders.of(this)[WeatherViewModel::class.java]
        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10*1000 *120
        mLocationRequest.fastestInterval = 2000

        checkLocationSetting()

    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            startLocationUpdates()
        }
    }

    /**
     *     Trigger new location updates at interval
     **/
    private fun startLocationUpdates() {

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        getFusedLocationProviderClient(this).requestLocationUpdates(
            mLocationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.lastLocation?.also {
                        //fetch weather using location
                        fetchWeatherFromLocation(it)
                    }
                }
            },
            Looper.myLooper()
        )
    }

    /**
       * Check if GPS is enabled or not and then show the dialog accordingly
     **/
    private fun checkLocationSetting(){
        googleApiClient =  GoogleApiClient.Builder(this@WeatherActivity)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this@WeatherActivity)
            .addOnConnectionFailedListener(this@WeatherActivity).build();
        googleApiClient.connect()


        val builder =  LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)

        builder.setAlwaysShow(true)

        val result =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnSuccessListener {
            startLocationUpdates()
        }
        result.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    it.startResolutionForResult(
                        this@WeatherActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    /**
    fetch weather information using the location object and update UI
     **/
    private fun fetchWeatherFromLocation(location : Location){
        AppCache.mUserCurrentLocation = location
        hideProgressDialog()
        showProgresDialog("Fetching weather")
        location.apply {  model.fetchWeather(location.latitude, location.longitude).observe(this@WeatherActivity,
            Observer<WeatherResponse> { weatherReponse ->
                updateWeather(weatherReponse)
            }) }
    }

    /**
        Update weather information on UI
     **/
    private fun updateWeather(weatherReponse : WeatherResponse){
        hideProgressDialog()
        temperatureView.text = (weatherReponse.main?.temp?.minus(273.15)?.toFloat()).toString()
        cityView.text = weatherReponse.name
        weatherReponse.weather.apply {
            cloudValueView.text = weatherReponse.weather?.get(0)?.description
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
        getFusedLocationProviderClient(this).removeLocationUpdates(LocationCallback())
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
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {

                        startLocationUpdates()
                    }
                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                    Log.i(TAG, "else block onRequestPermissionsResult ")
                }
            }
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnected(p0: Bundle?) {

    }

}

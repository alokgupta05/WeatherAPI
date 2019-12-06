package com.example.weatherapi.util

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationPermission : DialogInterface.OnClickListener {
    private var activity: AppCompatActivity? = null

    private var customListener: CustomListener? = null

    fun checkAndRequestPermission(
        appCompatActivity: AppCompatActivity,
        customListener: CustomListener
    ) {
        activity = appCompatActivity
        this.customListener = customListener
        if (isLocationPermissionEnabled(activity)) {
            fetchLocations()
        } else {
            requestLocationPermission()
        }
    }

    fun isLocationPermissionEnabled(activity: AppCompatActivity?): Boolean {
        return ContextCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            ActivityCompat.requestPermissions(
                this.activity!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            Log.e(TAG, "Application was denied permission!")

        }

    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this.activity!!,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocations()
            } else {
                Log.e(TAG, "Application was denied permission!")
            }

        }
    }

    private fun fetchLocations() {
        customListener!!.fetchLocation()

    }

    companion object {

        private val TAG = "LocationPermissions"
        val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

}

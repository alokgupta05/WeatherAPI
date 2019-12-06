package com.example.weatherapi.util

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log

import com.example.weatherapi.service.WeatherService


import android.content.Context.JOB_SCHEDULER_SERVICE

object Utils {

    private val DUMMY_ID = 1001


    fun scheduleJob(context: Context) {
        val componentName = ComponentName(context, WeatherService::class.java)
        val jobInfo = JobInfo.Builder(DUMMY_ID, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPeriodic((5 * 60).toLong())
            .build()

        val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = jobScheduler.schedule(jobInfo)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("scheduled", "Job scheduled!")
        } else {
            Log.d("not", "Job not scheduled")
        }
    }
}

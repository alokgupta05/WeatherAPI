package com.example.weatherapi.ext
import com.example.weatherapi.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient

/**
 * Add [Interceptor] if [BuildConfig.DEBUG] is true
 */
fun OkHttpClient.Builder.addInterceptorIfDebug(interceptor: Interceptor): OkHttpClient.Builder {
    return when {
        BuildConfig.DEBUG -> addInterceptor(interceptor)
        else -> this
    }
}

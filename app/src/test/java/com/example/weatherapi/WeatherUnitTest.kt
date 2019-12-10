package com.example.weatherapi

import androidx.lifecycle.MutableLiveData
import com.example.weatherapi.models.WeatherResponse
import com.example.weatherapi.repository.WeatherRepo
import com.example.weatherapi.viewmodel.WeatherViewModel
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.junit.Before
import org.mockito.Mockito.`when`
import java.lang.RuntimeException


class WeatherUnitTest {

    @Mock
    private lateinit var weatherRepo: WeatherRepo

    private lateinit var weatherViewModel: WeatherViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherViewModel = WeatherViewModel()
    }

    @Test
    fun fetchWeatherSuccessTest() {
        val weatherResponse = MutableLiveData<WeatherResponse>()
        `when`(weatherRepo.fetchWeather(18.00,135.00))
            .thenReturn(weatherResponse)
        assertEquals( weatherRepo.fetchWeather(18.00,135.00),weatherResponse)
    }

    @Test
    fun fetchWeatherFaliureTest() {
        `when`(weatherRepo.fetchWeather(0.00,0.00)).thenThrow(RuntimeException("Exception"))
        try {
            weatherRepo.fetchWeather(0.00,0.00)
        } catch (e: RuntimeException) {
            assertEquals("Exception", e.message)
        }
    }



}

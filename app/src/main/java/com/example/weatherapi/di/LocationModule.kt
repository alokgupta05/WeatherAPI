package com.example.weatherapi.di

import com.example.weatherapi.location.LocationServicableImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Singleton
    @Binds
    abstract fun bindsLocationServicable(impl: LocationServicableImpl): LocationServicable
}

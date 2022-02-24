package com.example.weatherapi.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineDispatchers {
        /**
         * Must be used for io executions like database, api calls, etc.
         */
        val io: CoroutineDispatcher

        /**
         * Must be used for computation executions
         */
        val default: CoroutineDispatcher

        /**
         * Must be used for main thread executions
         */
        val main: CoroutineDispatcher
    }

    object DefaultCoroutineDispatchers : CoroutineDispatchers {
        override val io = Dispatchers.IO
        override val default = Dispatchers.Default
        override val main = Dispatchers.Main
    }

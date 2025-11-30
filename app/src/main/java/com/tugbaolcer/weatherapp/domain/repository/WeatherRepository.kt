package com.tugbaolcer.weatherapp.domain.repository

import com.tugbaolcer.weatherapp.domain.model.WeatherData
import com.tugbaolcer.weatherapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherData>>
}
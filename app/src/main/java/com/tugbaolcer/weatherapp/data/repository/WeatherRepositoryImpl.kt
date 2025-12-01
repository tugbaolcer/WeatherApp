package com.tugbaolcer.weatherapp.data.repository

import android.util.Log
import com.tugbaolcer.weatherapp.data.local.dao.WeatherDao
import com.tugbaolcer.weatherapp.data.mapper.toDomain
import com.tugbaolcer.weatherapp.data.mapper.toEntity
import com.tugbaolcer.weatherapp.data.remote.api.WeatherApi
import com.tugbaolcer.weatherapp.domain.model.WeatherData
import com.tugbaolcer.weatherapp.domain.repository.WeatherRepository
import com.tugbaolcer.weatherapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao
) : WeatherRepository {

    override suspend fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherData>> = flow {
        emit(Resource.Loading)

        try {
            val response = api.getCurrentWeather(lat, lon)
            val currentWeatherData = response.toDomain()

            dao.insertWeather(currentWeatherData.toEntity())

            emit(Resource.Success(currentWeatherData))

        } catch (e: Exception) {

            val cachedWeatherData = dao.getWeather()?.toDomain()

            if (cachedWeatherData != null) {
                emit(Resource.Success(cachedWeatherData))
            } else {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }.flowOn(Dispatchers.IO)
}


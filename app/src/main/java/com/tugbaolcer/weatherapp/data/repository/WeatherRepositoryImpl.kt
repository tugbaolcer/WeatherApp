package com.tugbaolcer.weatherapp.data.repository

import com.tugbaolcer.weatherapp.data.api.WeatherApi
import com.tugbaolcer.weatherapp.domain.model.WeatherData
import com.tugbaolcer.weatherapp.domain.model.toDomain
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
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun fetchWeather(
        lat: Double,
        lon: Double
    ): Flow<Resource<WeatherData>> = flow {
        emit(Resource.Loading)

        try {
            val response = api.getCurrentWeather(lat, lon)
            val domain = response.toDomain()

            emit(Resource.Success(domain))

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)
}

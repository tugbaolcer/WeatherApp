package com.tugbaolcer.weatherapp.domain.model

import com.tugbaolcer.weatherapp.data.model.WeatherResponseDto

data class WeatherData(
    val location: Location,
    val current: CurrentWeather,
    val generatedAtMs: Long
)

data class Location(
    val latitude: Double,
    val longitude: Double
)

data class CurrentWeather(
    val temperatureCelsius: Double,
    val windSpeedKmh: Double,
    val windDirectionDegrees: Int,
)

fun WeatherResponseDto.toDomain() = WeatherData(
    location = Location(
        latitude = latitude,
        longitude = longitude
    ),
    current = CurrentWeather(
        temperatureCelsius = currentWeather.temperature,
        windSpeedKmh = currentWeather.windSpeed,
        windDirectionDegrees = currentWeather.windDirection
    ),
    generatedAtMs = System.currentTimeMillis()
)

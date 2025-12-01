package com.tugbaolcer.weatherapp.data.mapper

import com.tugbaolcer.weatherapp.data.local.entity.WeatherEntity
import com.tugbaolcer.weatherapp.data.remote.model.WeatherResponseDto
import com.tugbaolcer.weatherapp.domain.model.CurrentWeather
import com.tugbaolcer.weatherapp.domain.model.Location
import com.tugbaolcer.weatherapp.domain.model.WeatherData


fun WeatherEntity.toDomain() = WeatherData(
    location = Location(latitude, longitude),
    current = CurrentWeather(
        temperatureCelsius = temperature,
        windSpeedKmh = windSpeed,
        windDirectionDegrees = windDirection
    ),
    generatedAtMs = generatedAtMs
)

fun WeatherData.toEntity() = WeatherEntity(
    latitude = location.latitude,
    longitude = location.longitude,
    temperature = current.temperatureCelsius,
    windSpeed = current.windSpeedKmh,
    windDirection = current.windDirectionDegrees,
    generatedAtMs = generatedAtMs
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
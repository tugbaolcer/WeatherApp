package com.tugbaolcer.weatherapp.domain.model



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

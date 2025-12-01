package com.tugbaolcer.weatherapp.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponseDto(
    @field:Json(name = "latitude")
    val latitude: Double,
    @field:Json(name = "longitude")
    val longitude: Double,
    @field:Json(name = "current_weather")
    val currentWeather: CurrentWeatherDto
) {
    @JsonClass(generateAdapter = true)
    data class CurrentWeatherDto(
        @field:Json(name = "temperature")
        val temperature: Double,
        @field:Json(name = "windspeed")
        val windSpeed: Double,
        @field:Json(name = "winddirection")
        val windDirection: Int,
        @field:Json(name = "weathercode")
        val weatherCode: Int,
        @field:Json(name = "time")
        val time: String,
        @field:Json(name = "is_day")
        val isDay: Int
    )
}


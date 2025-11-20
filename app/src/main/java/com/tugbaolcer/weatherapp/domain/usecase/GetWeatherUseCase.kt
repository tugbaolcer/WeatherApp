package com.tugbaolcer.weatherapp.domain.usecase

import com.tugbaolcer.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repo: WeatherRepository
) {
    operator fun invoke(lat: Double, lon: Double) = repo.fetchWeather(lat = lat, lon = lon)
}

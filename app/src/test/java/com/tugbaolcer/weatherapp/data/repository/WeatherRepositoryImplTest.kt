package com.tugbaolcer.weatherapp.data.repository

import com.tugbaolcer.weatherapp.data.api.WeatherApi
import com.tugbaolcer.weatherapp.data.model.WeatherResponseDto
import com.tugbaolcer.weatherapp.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    private lateinit var api: WeatherApi
    private lateinit var repo: WeatherRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        repo = WeatherRepositoryImpl(api)
    }

    @Test
    fun fetchWeather_success() = runTest {
        val dto = WeatherResponseDto(
            latitude = 10.0,
            longitude = 20.0,
            currentWeather = WeatherResponseDto.CurrentWeatherDto(
                temperature = 15.0,
                windSpeed = 5.0,
                windDirection = 90,
                weatherCode = 0,
                time = "2025-11-20T12:00",
                isDay = 1
            )
        )

        coEvery { api.getCurrentWeather(10.0, 20.0) } returns dto

        val results = repo.fetchWeather(10.0, 20.0).toList()

        assert(results.first() is Resource.Loading)
        assert(results[1] is Resource.Success)
        assert((results[1] as Resource.Success).data.location.latitude == 10.0)
    }

    @Test
    fun fetchWeather_error() = runTest {
        coEvery { api.getCurrentWeather(any(), any()) } throws RuntimeException("boom")

        val results = repo.fetchWeather(0.0, 0.0).toList()

        assert(results.first() is Resource.Loading)
        assert(results[1] is Resource.Error)
    }
}
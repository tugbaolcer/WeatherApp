package com.tugbaolcer.weatherapp.repository

import app.cash.turbine.test
import com.tugbaolcer.weatherapp.data.api.WeatherApi
import com.tugbaolcer.weatherapp.data.model.WeatherResponseDto
import com.tugbaolcer.weatherapp.data.repository.WeatherRepositoryImpl
import com.tugbaolcer.weatherapp.domain.model.CurrentWeather
import com.tugbaolcer.weatherapp.domain.model.Location
import com.tugbaolcer.weatherapp.domain.model.WeatherData
import com.tugbaolcer.weatherapp.domain.repository.WeatherRepository
import com.tugbaolcer.weatherapp.rule.MainDispatcherRule
import com.tugbaolcer.weatherapp.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryTest {

    @get:Rule
    val dispatcher = MainDispatcherRule()

    private lateinit var api: WeatherApi
    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        api = mockk()
        repository = WeatherRepositoryImpl(api)
    }

    /**
     * Başarılı senaryo testi
      */

    @Test
    fun `fetchWeather success should emit Loading then Success`() = runTest {
        // Mock DTO
        val fakeDto = WeatherResponseDto(
            latitude = 41.0,
            longitude = 29.0,
            currentWeather = WeatherResponseDto.CurrentWeatherDto(
                temperature = 20.0,
                windSpeed = 5.0,
                windDirection = 180,
                weatherCode = 0,
                time = "2025-11-30T17:00",
                isDay = 1
            )
        )

        coEvery { api.getCurrentWeather(any(), any()) } returns fakeDto

        repository.fetchWeather(41.0, 29.0).test {
            val loading = awaitItem()
            assertEquals(Resource.Loading, loading)

            val success = awaitItem() as Resource.Success

            assertEquals(41.0, success.data.location.latitude)
            assertEquals(29.0, success.data.location.longitude)
            assertEquals(20.0, success.data.current.temperatureCelsius)
            assertEquals(5.0, success.data.current.windSpeedKmh)
            assertEquals(180, success.data.current.windDirectionDegrees)

            awaitComplete()
        }
    }

    /**
     * Hata senaryosu testi
     */
    @Test
    fun `fetchWeather error should emit Loading then Error`() = runTest {

        coEvery { api.getCurrentWeather(any(), any()) } throws RuntimeException("Network Failed")

        repository.fetchWeather(10.0, 10.0).test {
            assert(awaitItem() == Resource.Loading)
            val error = awaitItem()
            assert(error is Resource.Error && error.message == "Network Failed")
            awaitComplete()
        }
    }
}
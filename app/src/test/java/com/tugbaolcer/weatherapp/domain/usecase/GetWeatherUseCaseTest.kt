package com.tugbaolcer.weatherapp.domain.usecase

import com.tugbaolcer.weatherapp.domain.repository.WeatherRepository
import com.tugbaolcer.weatherapp.utils.Resource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetWeatherUseCaseTest {

    private lateinit var repo: WeatherRepository
    private lateinit var useCase: GetWeatherUseCase

    @Before
    fun setup() {
        repo = mockk()
        useCase = GetWeatherUseCase(repo)
    }

    @Test
    fun `invoke calls repo with correct params`() = runTest {
        val flow = flowOf(Resource.Loading)
        every { repo.fetchWeather(10.0, 20.0) } returns flow

        val result = useCase(10.0, 20.0)

        assert(result == flow)
        verify { repo.fetchWeather(10.0, 20.0) }
    }
}

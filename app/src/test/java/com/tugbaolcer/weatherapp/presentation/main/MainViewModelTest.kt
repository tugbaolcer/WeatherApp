package com.tugbaolcer.weatherapp.presentation.main

import app.cash.turbine.test
import com.tugbaolcer.weatherapp.domain.model.CurrentWeather
import com.tugbaolcer.weatherapp.domain.model.Location
import com.tugbaolcer.weatherapp.domain.model.WeatherData
import com.tugbaolcer.weatherapp.domain.usecase.GetWeatherUseCase
import com.tugbaolcer.weatherapp.utils.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var useCase: GetWeatherUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        useCase = mockk()
        viewModel = MainViewModel(useCase)
    }

    @Test
    fun `loadWeather sets loading then success`() = runTest {
        val weather = WeatherData(
            location = Location(10.0, 20.0),
            current = CurrentWeather(15.0, 5.0, 90),
            generatedAtMs = 0
        )

        val flow = flow {
            emit(Resource.Loading)
            emit(Resource.Success(weather))
        }

        every { useCase(10.0, 20.0) } returns flow

        viewModel.uiState.test {
            awaitItem()
            viewModel.loadWeather(10.0, 20.0)

            assert(awaitItem().loading)          // Loading
            val successState = awaitItem()

            assert(successState.weather == weather)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `loadWeather sets error when failed`() = runTest {
        val flow = flow {
            emit(Resource.Loading)
            emit(Resource.Error("fail"))
        }

        every { useCase(10.0, 20.0) } returns flow

        viewModel.uiState.test {
            awaitItem()
            viewModel.loadWeather(10.0, 20.0)

            awaitItem() // loading
            val errorState = awaitItem()

            assert(errorState.error == "fail")
            cancelAndConsumeRemainingEvents()
        }
    }
}

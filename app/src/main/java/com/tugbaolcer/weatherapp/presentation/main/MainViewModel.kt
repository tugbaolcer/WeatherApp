package com.tugbaolcer.weatherapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tugbaolcer.weatherapp.domain.model.WeatherData
import com.tugbaolcer.weatherapp.domain.usecase.GetWeatherUseCase
import com.tugbaolcer.weatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeatherUiState(
    val loading: Boolean = false,
    val weather: WeatherData? = null,
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            getWeatherUseCase(lat = lat, lon = lon).collect { res ->
                when (res) {
                    is Resource.Loading ->
                        _uiState.value = WeatherUiState(loading = true)

                    is Resource.Success ->
                        _uiState.value = WeatherUiState(weather = res.data)

                    is Resource.Error ->
                        _uiState.value = WeatherUiState(error = res.message)
                }
            }
        }
    }
}
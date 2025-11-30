package com.tugbaolcer.weatherapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tugbaolcer.weatherapp.domain.model.WeatherData
import com.tugbaolcer.weatherapp.domain.usecase.GetWeatherUseCase
import com.tugbaolcer.weatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<WeatherData>>(Resource.Loading)
    val uiState: StateFlow<Resource<WeatherData>> = _uiState

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            getWeatherUseCase(lat, lon)
                .onStart { _uiState.emit(Resource.Loading) }
                .collect { result ->
                    _uiState.emit(result)
                }
        }
    }
}
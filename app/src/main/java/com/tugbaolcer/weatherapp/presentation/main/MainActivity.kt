package com.tugbaolcer.weatherapp.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tugbaolcer.weatherapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.loadWeather(lat = 41.015137, lon = 28.979530)

        observeWeatherState()

    }

    private fun observeWeatherState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    // 1) Loading
                    if (state.loading) {
                        Log.d("WeatherLog", "Loading...")
                    }

                    // 2) Success
                    state.weather?.let { weather ->
                        Log.d("WeatherLog", "Data: $weather")
                    }

                    // 3) Error
                    state.error?.let { err ->
                        Log.e("WeatherLog", "Error: $err")
                    }
                }
            }
        }
    }
}
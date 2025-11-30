package com.tugbaolcer.weatherapp.presentation.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tugbaolcer.weatherapp.R
import com.tugbaolcer.weatherapp.databinding.ActivityMainBinding
import com.tugbaolcer.weatherapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        enableEdgeToEdge()

        collectState()

        // İstanbul koordinatları
        viewModel.loadWeather(lat = 41.015137, lon = 28.979530)
    }

    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is Resource.Success -> {
                            binding.weatherData = state.data
                        }
                        is Resource.Error -> {
                            binding.weatherData = null
                        }
                        is Resource.Loading -> {
                            binding.weatherData = null
                        }

                    }
                }
            }
        }
    }
}
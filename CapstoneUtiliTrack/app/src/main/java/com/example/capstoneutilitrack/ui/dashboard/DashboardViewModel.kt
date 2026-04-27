package com.example.capstoneutilitrack.ui.dashboard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneutilitrack.data.repository.DashboardRepository
import com.example.capstoneutilitrack.model.DashboardDto
import com.example.capstoneutilitrack.model.ForecastDetailsDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    var dashboardState by mutableStateOf<DashboardDto?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var forecastDetails by mutableStateOf<ForecastDetailsDto?>(null)
        private set

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = repository.getDashboard()

            result.onSuccess {
                forecastDetails = it.forecastDetails
                dashboardState = it
            }.onFailure {
                errorMessage = it.message ?: "Failed to load dashboard"
            }

            isLoading = false
        }
    }
}

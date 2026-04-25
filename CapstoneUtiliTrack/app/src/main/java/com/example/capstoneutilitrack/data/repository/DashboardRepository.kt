package com.example.capstoneutilitrack.data.repository

import com.example.capstoneutilitrack.data.remote.DashboardResponse
import com.example.capstoneutilitrack.data.network.DashboardApi
import com.example.capstoneutilitrack.model.DashboardDto
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val service: DashboardApi
) {
    suspend fun getDashboard(): Result<DashboardDto> {
        return try {
            val resp = service.getDashboardData()

            if (resp.isSuccessful) {
                val body: DashboardResponse? = resp.body()
                if (body != null) {
                    Result.success(body.toDto())
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("API Error ${resp.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

package com.example.capstoneutilitrack.data.network

import com.example.capstoneutilitrack.data.remote.DashboardResponse
import retrofit2.Response
import retrofit2.http.GET

interface DashboardApi {
    @GET("/api/Dashboard")
    suspend fun getDashboardData(): Response<DashboardResponse>
}
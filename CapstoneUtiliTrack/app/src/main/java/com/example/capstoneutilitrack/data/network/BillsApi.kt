package com.example.capstoneutilitrack.data.network

import com.example.capstoneutilitrack.data.remote.UtilityResponse
import com.example.capstoneutilitrack.model.BillDetailsDto
import com.example.capstoneutilitrack.model.BillsDtos
import retrofit2.Response
import retrofit2.http.*

data class BillsDashboardResponse(
    val glanceCard: BillsGlanceResponse?,
    val utilities: List<UtilityResponse>?
)

data class BillDetailsResponse(
    val id: String,
    val type: String?,
    val status: String?,
    val cost: Double?,
    val currency: String?,

    val nextBillDate: String?,

    val consumption: Double?,
    val consumptionUnit: String?,
    val pricePerUnit: String?,

    val weeklyConsumption: List<Int>?,
    val providerName: String?,
    val providerPhone: String?,
    val providerWebsite: String?
)

data class BillsGlanceResponse(
    val totalAmountDue: Double?,
    val amountPaid: Double?,
    val amountLeft: Double?,
    val nextBillDate: String?,
    val currency: String?
)

interface BillsApi {
    @GET("api/Bills/dashboard")
    suspend fun getDashboard(): Response<BillsDashboardResponse>

    @GET("api/Bills/{id}")
    suspend fun getBill(@Path("id") id: String): Response<BillDetailsResponse>

    @POST("api/Bills/{id}/pay")
    suspend fun payBill(@Path("id") id: String): Response<Unit>

}

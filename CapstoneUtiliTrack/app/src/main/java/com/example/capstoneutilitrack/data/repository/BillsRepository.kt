package com.example.capstoneutilitrack.data.repository

import com.example.capstoneutilitrack.data.network.BillDetailsResponse
import com.example.capstoneutilitrack.data.network.BillsApi
import com.example.capstoneutilitrack.data.network.BillsGlanceResponse
import com.example.capstoneutilitrack.data.remote.UtilityResponse
import com.example.capstoneutilitrack.model.*
import com.example.capstoneutilitrack.ui.bills.parseDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillsRepository @Inject constructor(
    private val api: BillsApi
) {
    suspend fun getBills(): BillsDtos {
        val response = api.getDashboard()

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Empty response")

            return BillsDtos(
                glanceCard = BillsGlanceModelDto(
                    totalAmountDue = body.glanceCard?.totalAmountDue ?: 0.0,
                    amountPaid = body.glanceCard?.amountPaid ?: 0.0,
                    amountLeft = calculateLeft(
                        body.glanceCard?.amountPaid,
                        body.glanceCard?.totalAmountDue
                    ),
                    progressPercentage = calculateProgress(
                        body.glanceCard?.amountPaid,
                        body.glanceCard?.totalAmountDue
                    ),
                    nextBillDate = Date(),
                    currency = body.glanceCard?.currency ?: "USD"
                ),
                utilities = body.utilities?.map { it.toDto() } ?: emptyList()
            )

        } else {
            throw Exception("Failed to fetch bills: ${response.code()}")
        }
    }

    private fun calculateLeft(amountPaid: Double?, totalAmountDue: Double?): Double {
        return (totalAmountDue ?: 0.0) - (amountPaid ?: 0.0)
    }

    suspend fun getBillDetails(id: String): BillDetailsDto {
        val response = api.getBill(id)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Empty response")
            return body.toDto()
        } else {
            throw Exception("Failed to fetch bill details")
        }
    }

    private fun calculateProgress(paid: Double?, total: Double?): Int {
        if (paid == null || total == null || total == 0.0) return 0
        return ((paid / total) * 100).toInt()
    }

    private fun BillDetailsResponse.toDto(): BillDetailsDto {

        val mappedStatus = when ((status ?: "").lowercase()) {
            "paid" -> Status.Paid
            "pending" -> Status.Pending
            "overdue" -> Status.Overdue
            else -> Status.Pending
        }

        val mappedType = try {
            MeterType.valueOf((type ?: "ELECTRICITY").uppercase())
        } catch (e: Exception) {
            MeterType.ELECTRICITY
        }

        return BillDetailsDto(
            id = id,
            type = mappedType,
            status = mappedStatus,
            cost = cost ?: 0.0,
            currency = currency ?: "$",

            nextBillDate = parseDate(nextBillDate),

            consumption = consumption ?: 0.0,
            consumptionUnit = consumptionUnit ?: "",
            pricePerUnit = pricePerUnit ?: "",

            weeklyConsumption = weeklyConsumption ?: emptyList(),

            providerName = providerName ?: "",
            providerPhone = providerPhone ?: "",
            providerWebsite = providerWebsite ?: ""
        )
    }

}

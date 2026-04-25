package com.example.capstoneutilitrack.data.remote

import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.*
import com.example.capstoneutilitrack.ui.bills.parseDate
import java.util.*

data class DashboardResponse(
    val glanceCard: GlanceModelResponse,
    val utilities: List<UtilityResponse>,
    val chartData: List<Double>,
    val topExpenses: List<ExpenseDto>,
    val insights: InsightsResponse,
    val forecast: UpcomingForecastResponse,
    val forecastDetails: ForecastDetailsResponse?
) {
    fun toDto(): DashboardDto {
        return DashboardDto(
            glanceCard = glanceCard.toDto(),
            utilities = utilities.map { it.toDto() },
            chartData = chartData,
            topExpenses = topExpenses,
            insights = insights.toDto(),
            forecast = forecast.toDto(),
            forecastDetails = forecastDetails?.toDto()
        )
    }
}

data class UpcomingForecastResponse(
    val nextMonthAmount: Double?,
    val percentageChange: Double?
){
    fun toDto(): UpcomingForecastDto {
        return UpcomingForecastDto(
            nextMonthAmount = nextMonthAmount ?: 0.0,
            percentageChange = percentageChange ?: 0.0
        )
    }
}

data class InsightsResponse(
    val values: List<Float>?,
    val monthValue: Double?,
    val changePercentage: Double?
) {
    fun toDto(): InsightsDto {
        return InsightsDto(
            values = values ?: emptyList(),
            MonthValue = monthValue ?: 0.0,
            ChangePercentage = changePercentage ?: 0.0
        )
    }
}

data class ForecastDetailsResponse(
    val items: List<MonthlyForecastItemResponse>?
) {
    fun toDto(): ForecastDetailsDto {
        return ForecastDetailsDto(
            items = items?.map { it.toDto() } ?: emptyList()
        )
    }
}

data class MonthlyForecastItemResponse(
    val month: String?,
    val amount: Double?,
    val isForecast: Boolean?,
    val changePercentage: Double?
) {
    fun toDto(): MonthlyForecastItemDto {
        return MonthlyForecastItemDto(
            month = month ?: "",
            amount = amount ?: 0.0,
            isForecast = isForecast ?: false,
            changePercentage = changePercentage ?: 0.0
        )
    }
}

data class UtilityResponse(
    val id: String,
    val name: String?,
    val type: String?,
    val currency: String?,
    val cost: Double?,
    val consumption: Double?,
    val consumptionUnit: String?,
    val status: String?,
    val nextBillDate: String?,
    val pricePerUnit: String?,
    val startDate: String?,
    val pastMonthChange: Double?,
    val forecastChange: Double?
) {
    fun toDto(): UtilityModelDto {
        val mt = runCatching { MeterType.valueOf((type ?: "").uppercase(Locale.US)) }.getOrElse { MeterType.ELECTRICITY }
        val st = when ((status ?: "").lowercase()) {
            "paid" -> Status.Paid
            "overdue" -> Status.Overdue
            "pending" -> Status.Pending
            else -> Status.Pending
        }

        return UtilityModelDto(
            id=id,
            name = name ?: "",
            type = mt,
            currency = (currency ?: "USD"),
            cost = cost ?: 0.0,
            consumption = consumption ?: 0.0,
            consumptionUnit = consumptionUnit ?: "",
            status = st,
            nextBillDate = parseDate(nextBillDate),
            pricePerUnit = pricePerUnit ?: "",
            startDate = parseDate(startDate),
            pastMonthChange=pastMonthChange?: 0.0,
            forecastChange=forecastChange?:0.0
        )
    }
}

data class GlanceModelResponse(
    val currentConsumption: Double?,
    val forecastedConsumption: Double?,
    val consumptionTrend: Double?,
    val forecastedConsumptionTrend: Double?,
    val currency: String?,
    val firstName: String?
) {
    fun toDto() = GlanceModelDto(
        currentConsumption ?: 0.0,
        forecastedConsumption ?: 0.0,
        consumptionTrend ?: 0.0,
        forecastedConsumptionTrend ?: 0.0,
        currency ?: "USD",
        firstName ?: "User"
    )
}

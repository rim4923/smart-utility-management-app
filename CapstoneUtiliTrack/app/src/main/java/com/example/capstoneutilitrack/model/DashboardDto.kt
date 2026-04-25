package com.example.capstoneutilitrack.model

import java.util.Date

data class DashboardDto(
    val glanceCard: GlanceModelDto,
    val utilities: List<UtilityModelDto>,
    val chartData: List<Double>,
    val topExpenses: List<ExpenseDto>,
    val insights: InsightsDto,
    val forecast: UpcomingForecastDto,
    val forecastDetails: ForecastDetailsDto?
)
data class GlanceModelDto(
    val currentConsumption: Double,
    val forecastedConsumption: Double,
    val consumptionTrend: Double,
    val forecastedConsumptionTrend: Double,
    val currency: String,
    val firstName: String,
)
data class InsightsDto(
    val values: List<Float>,

    val MonthValue: Double,
    val ChangePercentage: Double,
)

data class UpcomingForecastDto(
    val nextMonthAmount: Double,
    val percentageChange: Double
)
data class ExpenseDto(
    val type: MeterType,
    val amount: Double,
    val currency: String
)

data class UtilityModelDto(
    val id: String,
    val name: String,
    val status: Status,
    val type: MeterType,
    val currency: String,
    val cost: Double,

    val consumption: Double,
    val consumptionUnit: String,
    val nextBillDate: Date,
    val pricePerUnit: String,

    val startDate: Date,
    val pastMonthChange: Double?,
    val forecastChange: Double?,

)

data class MonthlyForecastItemDto(
    val month: String,
    val amount: Double,
    val isForecast: Boolean,
    val changePercentage: Double
)

data class ForecastDetailsDto(
    val items: List<MonthlyForecastItemDto>
)
package com.example.capstoneutilitrack.model
import java.util.Date

data class BillsDtos(
    val glanceCard: BillsGlanceModelDto,
    val utilities: List<UtilityModelDto>
)

data class BillsGlanceModelDto(
    val totalAmountDue: Double,
    val amountPaid: Double,
    val progressPercentage: Int,
    val amountLeft: Double,
    val nextBillDate: Date,
    val currency: String
)

data class FilterState(
    val type: String = "All",
    val startDate: String = "",
    val endDate: String = ""
)

enum class Status {
    Paid, Pending, Overdue
}

data class BillDetailsDto(
    val id: String,
    val type: MeterType,
    val status: Status,
    val cost: Double,
    val currency: String,

    val nextBillDate: Date,
    val consumption: Double,
    val consumptionUnit: String,
    val pricePerUnit: String,

    val weeklyConsumption: List<Int>,

    val providerName: String,
    val providerPhone: String,
    val providerWebsite: String
)
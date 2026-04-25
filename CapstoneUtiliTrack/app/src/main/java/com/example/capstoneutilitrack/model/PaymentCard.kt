package com.example.capstoneutilitrack.model

data class PaymentCardDto(
    val id: String,
    val holderName: String,
    val last4: String,
    val expMonth: Int,
    val expYear: Int,
    val brand: String = "VISA",
    val isDefault: Boolean = false
)

enum class PaymentMode {
    FULL,
    PROFILE
}
data class PaymentSession(
    val bills: List<UtilityModelDto>,
    val totalAmount: Double,
    val selectedCard: PaymentCardDto? = null
)

data class AddCardRequest(
    val holderName: String,
    val cardNumber: String,
    val expMonth: Int,
    val expYear: Int
)

data class PayBillsRequest(
    val billIds: List<String>
)

data class PayBillsResponse(
    val clientSecret: String,
    val paymentIntentId: String,
    val totalAmount: Double? = null,
    val currency: String? = null,
    val status: String? = null,
    val billCount: Int? = null,
    val requiresAction: Boolean? = null
)
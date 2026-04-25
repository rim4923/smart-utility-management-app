package com.example.capstoneutilitrack.data.repository

import com.example.capstoneutilitrack.data.network.PayBillsRequest
import com.example.capstoneutilitrack.data.network.PayBillsResponse
import com.example.capstoneutilitrack.data.network.PaymentApi
import com.example.capstoneutilitrack.data.network.PaymentMethodResponse
import com.example.capstoneutilitrack.model.AddCardRequest
import com.example.capstoneutilitrack.model.PaymentCardDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val api: PaymentApi
) {
    private fun map(r: PaymentMethodResponse) = PaymentCardDto(
        id = r.id,
        holderName = r.holderName,
        last4 = r.last4,
        expMonth = r.expMonth,
        expYear = r.expYear,
        brand = r.brand,
        isDefault = r.isDefault
    )

    suspend fun list(): List<PaymentCardDto> {
        val res = api.getPaymentMethods()
        if (res.isSuccessful && res.body() != null) {
            return res.body()!!.map { map(it) }
        }
        throw Exception(res.errorBody()?.string() ?: "Failed to load cards")
    }

    suspend fun payBills(billIds: List<String>): PayBillsResponse {
        val res = api.payBills(PayBillsRequest(billIds))

        if (res.isSuccessful && res.body() != null) {
            return res.body()!!
        }

        throw Exception(res.errorBody()?.string() ?: "Payment failed")
    }

    suspend fun addCard(card: AddCardRequest) {
        val res = api.addCard(card)
        if (!res.isSuccessful) {
            throw Exception(res.errorBody()?.string() ?: "Failed to add card")
        }
    }
}

package com.example.capstoneutilitrack.data.repository

import com.example.capstoneutilitrack.data.network.PayBillsRequest
import com.example.capstoneutilitrack.data.network.PayBillsResponse
import com.example.capstoneutilitrack.data.network.PaymentApi
import com.example.capstoneutilitrack.data.network.PaymentMethodResponse
import com.example.capstoneutilitrack.data.remote.safeApiCall
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
        val result = safeApiCall { api.getPaymentMethods() }
        return result.map { map(it) }
    }

    suspend fun payBills(billIds: List<String>): PayBillsResponse {
        return safeApiCall { api.payBills(PayBillsRequest(billIds)) }
    }

    suspend fun addCard(card: AddCardRequest) {
        safeApiCall { api.addCard(card) }
    }
}

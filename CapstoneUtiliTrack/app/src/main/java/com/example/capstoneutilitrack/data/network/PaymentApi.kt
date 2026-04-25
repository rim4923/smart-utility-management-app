package com.example.capstoneutilitrack.data.network

import com.example.capstoneutilitrack.model.AddCardRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Headers

data class PaymentMethodResponse(
    val id: String,
    val brand: String,
    val last4: String,
    val expMonth: Int,
    val expYear: Int,
    val holderName: String,
    val isDefault: Boolean
)

data class SetDefaultPaymentRequest(val paymentMethodId: String)

data class PayBillsRequest(val billIds: List<String>)

data class PayBillsResponse(
    val paymentIntentId: String,
    val clientSecret: String,
    val totalAmount: Double,
    val currency: String,
    val billCount: Int,
    val status: String,
    val requiresAction: Boolean
)

data class ConfirmBillsRequest(val paymentId: String)
data class ConfirmBillsResponse(val status: String, val acceptanceCode: String?)

interface PaymentApi {
    @GET("api/payments/methods")
    suspend fun getPaymentMethods(): Response<List<PaymentMethodResponse>>

    @POST("api/payments/methods")
    suspend fun addCard(@Body body: AddCardRequest): Response<Unit>
    @Headers("Accept: text/plain")
    @POST("api/payments/pay")
    suspend fun payBills(@Body body: PayBillsRequest): Response<PayBillsResponse>
}

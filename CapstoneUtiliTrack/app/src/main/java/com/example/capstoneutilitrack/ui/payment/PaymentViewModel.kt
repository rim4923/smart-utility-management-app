package com.example.capstoneutilitrack.ui.payment

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneutilitrack.data.repository.PaymentRepository
import com.example.capstoneutilitrack.model.AddCardRequest
import com.example.capstoneutilitrack.model.PaymentCardDto
import com.example.capstoneutilitrack.model.PaymentSession
import com.example.capstoneutilitrack.model.UtilityModelDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var paymentSuccess by mutableStateOf(false)
    var session by mutableStateOf<PaymentSession?>(null)
        private set

    var cards by mutableStateOf<List<PaymentCardDto>>(emptyList())
        private set

    var selectedCard by mutableStateOf<PaymentCardDto?>(null)
        private set

    fun startPaymentSession(bills: List<UtilityModelDto>) {
        session = PaymentSession(
            bills = bills,
            totalAmount = bills.sumOf { it.cost },
            selectedCard = selectedCard
        )
    }

    fun loadCards() {
        viewModelScope.launch {
            try {
                cards = repository.list()
                selectedCard = cards.firstOrNull()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectCard(card: PaymentCardDto) {
        selectedCard = card

        session = session?.copy(selectedCard = card)
    }


    fun payBills(onSuccess: () -> Unit) {
        val currentSession = session ?: return

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                repository.payBills(currentSession.bills.map { it.id })
                paymentSuccess = true
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Payment failed"
            } finally {
                isLoading = false
            }
        }
    }
    fun resetPaymentState() {
        paymentSuccess = false
        session = null
    }
    fun addCard(
        cardNumber: String,
        holderName: String,
        expMonth: Int,
        expYear: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.addCard(
                    AddCardRequest(
                        holderName = holderName,
                        cardNumber = cardNumber,
                        expMonth = expMonth,
                        expYear = expYear
                    )
                )
                onSuccess()
            } catch (e: Exception) {
                val msg = when {
                    e.message?.contains("card", true) == true -> "Invalid card details"
                    e.message?.contains("network", true) == true -> "Network error. Try again."
                    else -> "Something went wrong. Please try again."
                }
                onError(msg)
            }
        }
    }

}


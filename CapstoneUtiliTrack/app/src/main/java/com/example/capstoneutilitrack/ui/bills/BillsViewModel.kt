package com.example.capstoneutilitrack.ui.bills

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneutilitrack.data.repository.BillsRepository
import com.example.capstoneutilitrack.model.BillDetailsDto
import com.example.capstoneutilitrack.model.BillsDtos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillsViewModel @Inject constructor(
    private val repository: BillsRepository
) : ViewModel() {

    var billsState by mutableStateOf<BillsDtos?>(null)
        private set

    var selectedBillDetails by mutableStateOf<BillDetailsDto?>(null)
        private set

    fun clearDetails() {
        selectedBillDetails = null
    }
    fun loadBills() {
        viewModelScope.launch {
            try {
                billsState = repository.getBills()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadBillDetails(id: String) {
        viewModelScope.launch {
            try {
                selectedBillDetails = repository.getBillDetails(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

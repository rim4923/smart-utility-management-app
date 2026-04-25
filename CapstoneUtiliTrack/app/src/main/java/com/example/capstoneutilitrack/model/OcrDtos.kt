package com.example.capstoneutilitrack.model


data class OcrResponse(
    val passportNumber: String?,
    val isSuccess: Boolean,
    val errorMessage: String?,
    val confidenceScore: Double?
)
package com.example.capstoneutilitrack.data.remote

import retrofit2.Response
import java.net.SocketTimeoutException
import java.io.IOException

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): T {
    try {
        val response = call()

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response from server")
        } else {
            val error = response.errorBody()?.string()
            throw Exception(error ?: "Server error: ${response.code()}")
        }

    } catch (e: SocketTimeoutException) {
        throw Exception("Request timed out. Please try again.")
    } catch (e: IOException) {
        throw Exception("No internet connection.")
    } catch (e: Exception) {
        throw Exception(e.message ?: "Unexpected error")
    }
}
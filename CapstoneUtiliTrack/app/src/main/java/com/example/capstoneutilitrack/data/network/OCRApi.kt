package com.example.capstoneutilitrack.data.network

import com.example.capstoneutilitrack.model.OcrResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OcrApi {

    @Multipart
    @POST("scan-passport")
    suspend fun scanPassport(
        @Part file: MultipartBody.Part
    ): OcrResponse
}
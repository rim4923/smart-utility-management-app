package com.example.capstoneutilitrack.di

import com.example.capstoneutilitrack.data.network.ProfileApi
import com.example.capstoneutilitrack.data.network.TokenInterceptor
import com.example.capstoneutilitrack.data.network.DashboardApi
import com.example.capstoneutilitrack.data.network.AuthApi
import com.example.capstoneutilitrack.data.network.BillsApi
import com.example.capstoneutilitrack.data.network.OcrApi
import com.example.capstoneutilitrack.data.network.PaymentApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:5082/"

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    @Named("public")
    fun providePublicClient(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthClient(
        logging: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(tokenInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideGson(): com.google.gson.Gson =
        com.google.gson.GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()

    @Provides
    @Singleton
    @Named("public")
    fun providePublicRetrofit(
        @Named("public") client: OkHttpClient,
        gson: com.google.gson.Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Retrofit (auth)
    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthRetrofit(
        @Named("auth") client: OkHttpClient,
        gson: com.google.gson.Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides @Singleton
    fun provideAuthApi(@Named("public") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun provideProfileApi(@Named("auth") retrofit: Retrofit): ProfileApi =
        retrofit.create(ProfileApi::class.java)

    @Provides @Singleton
    fun provideBillsApi(@Named("auth") retrofit: Retrofit): BillsApi =
        retrofit.create(BillsApi::class.java)

    @Provides @Singleton
    fun provideDashboardApi(@Named("auth") retrofit: Retrofit): DashboardApi =
        retrofit.create(DashboardApi::class.java)

    @Provides
    @Singleton
    fun provideOcrApi(@Named("auth") retrofit: Retrofit): OcrApi =
        retrofit.create(OcrApi::class.java)

    @Provides @Singleton
    fun providePaymentApi(@Named("auth") retrofit: Retrofit): PaymentApi =
        retrofit.create(PaymentApi::class.java)
}

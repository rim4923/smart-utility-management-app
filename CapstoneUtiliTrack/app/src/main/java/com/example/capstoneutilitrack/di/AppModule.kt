package com.example.capstoneutilitrack.di

import com.example.capstoneutilitrack.data.network.ProfileApi
import com.example.capstoneutilitrack.data.repository.DashboardRepository
import com.example.capstoneutilitrack.data.repository.AuthRepository
import com.example.capstoneutilitrack.data.repository.ProfileRepository
import com.example.capstoneutilitrack.data.network.AuthApi
import com.example.capstoneutilitrack.data.network.DashboardApi
import com.example.capstoneutilitrack.data.network.OcrApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthApi,
        ocrApi: OcrApi
    ): AuthRepository {
        return AuthRepository(authService, ocrApi)
    }
    @Provides
    @Singleton
    fun provideDashboardRepository(service: DashboardApi): DashboardRepository =
        DashboardRepository(service)

    @Provides
    @Singleton
    fun provideProfileRepository(api: ProfileApi): ProfileRepository =
        ProfileRepository(api)


}

package com.example.capstoneutilitrack

import android.app.Application
import com.example.capstoneutilitrack.data.network.AccessTokenUtilities
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AccessTokenUtilities.init(this)
    }
}

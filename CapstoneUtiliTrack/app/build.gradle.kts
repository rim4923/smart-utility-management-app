plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.capstoneutilitrack"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.capstoneutilitrack"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("boolean", "USE_MOCK_STRIPE", "false")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.compose.foundation.layout)
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.47")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation ("androidx.appcompat:appcompat:1.4.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-text")
    implementation("androidx.compose.foundation:foundation")
    implementation("com.google.dagger:hilt-android:2.54")
    kapt("com.google.dagger:hilt-compiler:2.54")
    implementation("com.hbb20:ccp:2.6.0")
    implementation(libs.ccp)
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.18")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.material3:material3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material:1.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("co.yml:ycharts:2.1.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.stripe:stripe-android:21.22.1")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")
    implementation("com.patrykandpatrick.vico:compose:1.13.1")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
}
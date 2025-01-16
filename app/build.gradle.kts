import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.android.application")
    id("kotlin-kapt")
}

android {
    namespace = "com.voci.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.voci.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "MAPBOX_ACCESS_TOKEN", "\"${project.properties["MAPBOX_ACCESS_TOKEN"] ?: ""}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    val localProperties = Properties()
    localProperties.load(project.rootProject.file("local.properties").inputStream())
}

dependencies {
    // --------------------------------------------------
    // Android Jetpack Libraries
    // --------------------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Material Design
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended.v175)
    implementation(libs.material)
    implementation(libs.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Room Persistence
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // --------------------------------------------------
    // Firebase Libraries
    // --------------------------------------------------
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.crashlytics.buildtools)

    // --------------------------------------------------
    // Kotlin Coroutines
    // --------------------------------------------------
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // --------------------------------------------------
    // Google Play Services
    // --------------------------------------------------
    implementation(libs.play.services.base)
    implementation(libs.play.services.location)

    // --------------------------------------------------
    // Networking
    // --------------------------------------------------
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation (libs.gson)
    //implementation(libs.converter.gson) // Removed as it's included in retrofit

    // --------------------------------------------------
    // Mapbox Libraries
    // --------------------------------------------------
    implementation(libs.mapbox.android)
    implementation(libs.mapbox.maps.compose)
    implementation(libs.mapbox.autofill)
    implementation(libs.mapbox.discover)
    implementation(libs.mapbox.place.autocomplete)
    implementation(libs.mapbox.offline)
    implementation(libs.mapbox.search.android)
    implementation(libs.mapbox.search.android.ui)
    implementation(libs.mapbox.sdk.services)

    // --------------------------------------------------
    // Image Loading
    // --------------------------------------------------
    implementation(libs.coil.compose)

    // --------------------------------------------------
    // Testing
    // --------------------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}
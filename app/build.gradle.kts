plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.pv_movil"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.pv_movil"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // retrofit + gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // okhttp logging (para ver requests/responses en Logcat)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // coroutines (para las funciones suspend de los services)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
    // lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx")
    // security (para EncryptedSharedPreferences)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}
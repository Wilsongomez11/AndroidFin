import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.proyectofinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.proyectofinal"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
dependencies {

    // Navegación Compose
    implementation ("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")

// Hilt (Inyección de dependencias)
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-compiler:2.48")



    // Core Compose
    implementation ("androidx.activity:activity-compose:1.8.0")
    implementation ("androidx.compose.ui:ui:1.5.4")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation ("androidx.compose.material3:material3:1.1.2")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-compiler:2.48")
    implementation ("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.compose.ui:ui:1.0.0")
    implementation("androidx.compose.ui:ui-graphics:1.0.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.0")
    implementation ("androidx.compose.material:material:1.0.0")
    implementation("androidx.compose.material3:material3:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:compose-bom:1.0.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.0.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.0.0")
}

private fun DependencyHandlerScope.kapt(string: String) {


}


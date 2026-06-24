plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics.plugin)
    id("com.huawei.agconnect")
}

android {
    namespace = "com.dipdev.themutemaster"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dipdev.themutemaster"
        minSdk = 24
        targetSdk = 36
        versionCode = 6
        versionName = "1.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.generateKotlin", "true")
        }

    }

    flavorDimensions += "services"
    productFlavors {
        create("gms") {
            dimension = "services"
        }
        create("hms") {
            dimension = "services"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    "gmsImplementation"("com.google.android.gms:play-services-location:21.3.0")
    "hmsImplementation"("com.huawei.hms:location:6.16.0.302")
    "hmsImplementation"("com.huawei.hms:drm:2.6.0.306")
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation.layout)
    ksp(libs.hilt.android.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    "gmsImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    testImplementation(libs.junit)
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.robolectric:robolectric:4.12.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Firebase
    "gmsImplementation"(platform(libs.firebase.bom))
    "gmsImplementation"(libs.firebase.analytics)
    "gmsImplementation"(libs.firebase.crashlytics)
}

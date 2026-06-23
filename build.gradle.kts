buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://developer.huawei.com/repo/")
    }
    dependencies {
        classpath("com.huawei.agconnect:agcp:1.9.5.302")
    }
}

// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android.plugin) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics.plugin) apply false
}
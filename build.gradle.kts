// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Hilt Plugin (Project Level)
    // Change 2.51.1 to match your app-level (or use 2.52 which is stable)
    id("com.google.dagger.hilt.android") version "2.51" apply false

}
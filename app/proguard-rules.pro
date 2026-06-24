# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Firebase Crashlytics — preserve line numbers for readable stack traces
# CRITICAL: without this, crash reports show obfuscated line numbers
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Firebase Crashlytics — keep custom exceptions
-keep public class * extends java.lang.Exception

# Keep Firebase Crashlytics and components for R8
-keep class com.google.firebase.crashlytics.** { *; }
-keep class com.google.firebase.components.** { *; }

# Keep Room Database implementations
-keep class * extends androidx.room.RoomDatabase
-keep class **_Impl {
    <init>(...);
}

# Keep Huawei HMS components for R8
-keep class com.huawei.** { *; }
-dontwarn com.huawei.**

# Prevent DataStore serialization crash by keeping AppThemeMode enum names
-keep class com.dipdev.themutemaster.data.local.AppThemeMode { *; }
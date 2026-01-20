package com.dipdev.themutemaster.utils

import android.Manifest
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat

// --- CLIPBOARD ---
fun Context.copyToClipboard(text: String?, label: String = "Copied Text") {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clip)

    // Android 13+ shows its own clipboard UI, so we hide the toast to avoid duplication
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}

// --- LOCATION PERMISSIONS ---
fun Context.hasLocationPermission(): Boolean {
    val foreground = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val background = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
    return foreground && background
}

// --- GRANULAR LOCATION CHECKS ---

/**
 * Checks ONLY for Foreground Location (While In Use).
 * Does not check for Background location.
 */
fun Context.hasForegroundLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Checks ONLY for Background Location (Always allow).
 * Returns true automatically on Android 9 (Pie) and below, as it was bundled with Fine Location.
 */
fun Context.hasBackgroundLocationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // Pre-Android 10, Foreground permission implied Background permission
    }
}

// --- NOTIFICATION PERMISSION (Android 13+) ---
fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // Older Android versions grant this automatically
    }
}

// --- DND POLICY ACCESS (Critical for Muting) ---
/**
 * Checks if the app is allowed to change Do Not Disturb / Ringer modes.
 * Without this, audioManager.ringerMode = SILENT will crash or fail.
 */
fun Context.hasDndPermission(): Boolean {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.isNotificationPolicyAccessGranted
}

// --- SETTINGS NAVIGATORS ---

/**
 * Opens the App Details page in Settings.
 * Useful when the user permanently denies a permission.
 */
fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}

/**
 * Opens the specific "Do Not Disturb Access" settings page.
 * You CANNOT request this permission via a dialog popup; you must send the user here.
 */
fun Context.openDndSettings() {
    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}
package com.dipdev.themutemaster.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume


class DefaultLocationClient (
    private val context: Context,
    private val client: FusedLocationProviderClient
): LocationClient{
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnabled){
            throw LocationClient.LocationException("GPS not enabled!")
        }
        return try {
            client.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()
        } catch (e: Exception){
            throw LocationClient.LocationException(e.message ?:"Unknown Error!!!!")
        }
    }
    @Suppress("DEPRECATION")
    override suspend fun getAddressFromCoordinates(lat: Double, long: Double): String? {
        // 1. Safety Check: Does this device even have a Geocoder?
        if (!Geocoder.isPresent()) return "Geocoder not supported on this device"

        val geocoder = Geocoder(context, Locale.getDefault())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // --- NEW WAY (Android 13+) ---
            // This converts the "Callback" into a "Suspend Function"
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(lat, long, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        continuation.resume(addresses[0].getAddressLine(0))
                    } else {
                        continuation.resume("Unknown Location")
                    }
                }
            }
        } else {
            // --- OLD WAY (Android 12 and below) ---
            // We must manually move this to IO thread or it freezes the UI
            withContext(Dispatchers.IO) {
                try {
                    val addresses = geocoder.getFromLocation(lat, long, 1)
                    if (!addresses.isNullOrEmpty()) {
                        addresses[0].getAddressLine(0)
                    } else {
                        "Unknown Location"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Address lookup failed"
                }
            }
        }
    }

}
package com.dipdev.themutemaster.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationRequest
import com.dipdev.themutemaster.utils.CrashReporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HmsLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient,
    private val crashReporter: CrashReporter
) : LocationClient {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnabled) {
            throw LocationClient.LocationException("GPS not enabled!")
        }
        
        return try {
            suspendCancellableCoroutine { continuation ->
                // HMS LocationKit Task
                val task = client.lastLocation
                task.addOnSuccessListener { location ->
                    continuation.resume(location)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
            }
        } catch (e: Exception) {
            crashReporter.recordNonFatal(e, context = "HmsLocationClient.getCurrentLocation")
            throw LocationClient.LocationException(e.message ?: "Unknown Error!!!!")
        }
    }

    @Suppress("DEPRECATION")
    override suspend fun getAddressFromCoordinates(lat: Double, long: Double): String? {
        if (!Geocoder.isPresent()) return "Geocoder not supported on this device"

        val geocoder = Geocoder(context, Locale.getDefault())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
            withContext(Dispatchers.IO) {
                try {
                    val addresses = geocoder.getFromLocation(lat, long, 1)
                    if (!addresses.isNullOrEmpty()) {
                        addresses[0].getAddressLine(0)
                    } else {
                        "Unknown Location"
                    }
                } catch (e: Exception) {
                    crashReporter.recordNonFatal(e, context = "HmsLocationClient.getAddressFromCoordinates (API < 33)")
                    "Address lookup failed"
                }
            }
        }
    }
}

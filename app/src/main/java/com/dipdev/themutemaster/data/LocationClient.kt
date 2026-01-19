package com.dipdev.themutemaster.data

import android.location.Location

interface LocationClient{
    suspend fun getCurrentLocation(): Location?
    suspend fun getAddressFromCoordinates(lat: Double,long: Double): String?
    class LocationException(message:String): Exception(message)
}
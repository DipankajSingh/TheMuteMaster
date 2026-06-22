package com.dipdev.themutemaster.di

import android.content.Context
import com.dipdev.themutemaster.data.HmsLocationClient
import com.dipdev.themutemaster.data.LocationClient
import com.dipdev.themutemaster.data.GeofenceManager
import com.dipdev.themutemaster.data.HmsGeofenceManager
import com.dipdev.themutemaster.utils.CrashReporter
import com.dipdev.themutemaster.utils.HmsCrashReporter
import com.huawei.hms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceBindingMod {
    @Binds
    @Singleton
    abstract fun bindCrashReporter(impl: HmsCrashReporter): CrashReporter

    @Binds
    @Singleton
    abstract fun bindGeofenceManager(impl: HmsGeofenceManager): GeofenceManager
}

@Module
@InstallIn(SingletonComponent::class)
object ServiceProvidingMod {
    @Provides
    @Singleton
    fun provideLocationClient(
        @ApplicationContext context: Context,
        crashReporter: CrashReporter
    ): LocationClient {
        return HmsLocationClient(
            context,
            LocationServices.getFusedLocationProviderClient(context),
            crashReporter
        )
    }
}

package com.dipdev.themutemaster.di

import android.content.Context
import com.dipdev.themutemaster.data.DefaultLocationClient
import com.dipdev.themutemaster.data.LocationClient
import com.dipdev.themutemaster.utils.CrashReporter
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationMod{
    @Provides
    @Singleton
    fun provideLocationClient(
        @ApplicationContext context: Context,
        crashReporter: CrashReporter
    ): LocationClient{
        return DefaultLocationClient(
            context,
            LocationServices.getFusedLocationProviderClient(context),
            crashReporter
        )
    }
}
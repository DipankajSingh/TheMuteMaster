package com.dipdev.themutemaster.di

import android.app.Application
import androidx.room.Room
import com.dipdev.themutemaster.data.local.GeofenceDao
import com.dipdev.themutemaster.data.local.GeofenceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): GeofenceDatabase {
        return Room.databaseBuilder(
            app,
            GeofenceDatabase::class.java,
            "geofence_db.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: GeofenceDatabase): GeofenceDao {
        return db.dao
    }
}
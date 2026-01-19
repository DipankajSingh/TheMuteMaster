package com.dipdev.themutemaster.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GeofenceEntity::class],
    version = 1,
    exportSchema = true
)
abstract class GeofenceDatabase : RoomDatabase() {
    abstract val dao: GeofenceDao
}
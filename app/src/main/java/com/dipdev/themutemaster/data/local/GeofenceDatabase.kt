package com.dipdev.themutemaster.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [GeofenceEntity::class, ScheduleEntity::class],
    version = 2,
    exportSchema = true
)
abstract class GeofenceDatabase : RoomDatabase() {
    abstract val dao: GeofenceDao
    abstract val scheduleDao: ScheduleDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `schedules` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `name` TEXT NOT NULL, 
                        `startTimeMins` INTEGER NOT NULL, 
                        `endTimeMins` INTEGER NOT NULL, 
                        `daysOfWeek` TEXT NOT NULL, 
                        `isEnabled` INTEGER NOT NULL, 
                        `ringerMode` INTEGER, 
                        `muteMedia` INTEGER NOT NULL, 
                        `customMediaVolumePercent` INTEGER
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
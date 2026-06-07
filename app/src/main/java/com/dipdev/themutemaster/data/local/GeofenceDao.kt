
package com.dipdev.themutemaster.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the 'geofences' table.
 * This interface defines all the database interactions for the app.
 * Room generates the implementation of this class at compile time.
 */
@Dao
interface GeofenceDao {



    /**
     * Inserts a new geofence or updates an existing one.
     * * @param geofence The entity to save.
     * @see OnConflictStrategy.REPLACE If a row with the same ID already exists,
     * it will be overwritten (Updated). This handles both 'Add' and 'Edit' cases.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(geofence: GeofenceEntity):Long

    /**
     * Deletes a specific geofence from the database.
     * Room matches the ID of the passed id to find the row to delete.
     *
     * @param id The entity to remove.
     */
    @Query("DELETE from geofences WHERE id= :id")
    suspend fun deleteGeofenceById(id: Int): Int

    /**
     * Observes all geofences in the database.
     * * This returns a [Flow], which is a reactive stream.
     * Whenever the database changes (insert/delete), this Flow emits the new list
     * immediately. The UI should collect this to update the list automatically.
     *
     * @return A stream of List<GeofenceEntity>
     */
    @Query("SELECT * FROM geofences")
    fun getAllGeofences(): Flow<List<GeofenceEntity>>

    /**
     * Fetches ONLY the geofences that are currently active (isEnabled = true).
     * * This is a "One-Shot" suspend function, not a Flow. It runs once and returns.
     * USE CASE: The BootReceiver calls this when the phone restarts to
     * re-register alarms with the Android System.
     *
     * @return A static List<GeofenceEntity> of active zones.
     */
    @Query("SELECT * FROM geofences WHERE isEnabled = 1")
    suspend fun getAllEnabledGeofencesOneShot(): List<GeofenceEntity>

    /**
     * Fetches ALL geofences (Enabled + Disabled) once.
     *
     * USE CASE: Logic Validation. Before saving a new location, we call this
     * to check if the user is trying to save a duplicate location that might
     * happen to be disabled.
     *
     * @return A static List<GeofenceEntity> of all zones.
     */
    @Query("SELECT * FROM geofences")
    suspend fun getAllGeofencesOneShot(): List<GeofenceEntity>

    @Query("SELECT * FROM geofences WHERE id = :id")
    suspend fun getGeofenceById(id: Int): GeofenceEntity?

    @Delete
    suspend fun deleteGeofence(geofence: GeofenceEntity): Int
}
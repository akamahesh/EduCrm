package com.crm.edu.data.leaves.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LeavesDao {

    @Query(value = "SELECT * FROM leaves_data")
    fun getHolidayEntities(): Flow<List<LeaveEntity>>

    /**
     * Inserts [holidayEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreHolidays(holidayEntities: List<LeaveEntity>): List<Long>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertHolidays(entities: List<LeaveEntity>)
}
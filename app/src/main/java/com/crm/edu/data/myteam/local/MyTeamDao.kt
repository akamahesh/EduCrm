package com.crm.edu.data.myteam.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MyTeamDao {

    @Query(value = "SELECT * FROM staff_attendance_data")
    fun getStaffAttendanceData(): Flow<List<StaffAttendanceEntity>>

    /**
     * Inserts [holidayEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreStaffAttendanceData(holidayEntities: List<StaffAttendanceEntity>): List<Long>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertHolidays(entities: List<StaffAttendanceEntity>)
}
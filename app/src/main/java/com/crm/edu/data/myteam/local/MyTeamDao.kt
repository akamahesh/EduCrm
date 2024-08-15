package com.crm.edu.data.myteam.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MyTeamDao {

    @Query(value = "SELECT * FROM staff_attendance_data")
    fun getStaffAttendanceData(): Flow<List<StaffAttendanceEntity>>


    @Query(value = "DELETE FROM staff_attendance_data")
    suspend fun clearStaffAttendanceData()

    /**
     * Inserts [holidayEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreStaffAttendanceData(entities: List<StaffAttendanceEntity>): List<Long>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertStaffAttendanceData(entities: List<StaffAttendanceEntity>)


    @Transaction
    suspend fun clearAndInsertStaffAttendanceData(entities: List<StaffAttendanceEntity>): List<Long> {
        clearStaffAttendanceData()
        return insertOrIgnoreStaffAttendanceData(entities)
    }
}
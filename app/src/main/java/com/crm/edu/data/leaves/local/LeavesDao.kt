package com.crm.edu.data.leaves.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LeavesDao {

    @Query(value = "SELECT * FROM leaves_data")
    fun getStaffLeaveData(): Flow<List<LeaveEntity>>

    @Query("DELETE FROM leaves_data")
    suspend fun clearLeaveTable()

    /**
     * Inserts [leaveEntity] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreStaffLeaveData(leaveEntity: List<LeaveEntity>): List<Long>

    @Transaction
    suspend fun clearAndInsertStaffLeaveData(leaveEntity: List<LeaveEntity>): List<Long> {
        clearLeaveTable()
        return insertOrIgnoreStaffLeaveData(leaveEntity)
    }

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertLeaveData(entities: List<LeaveEntity>)
}
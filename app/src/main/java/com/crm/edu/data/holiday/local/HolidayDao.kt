package com.crm.edu.data.holiday.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HolidayDao {

    @Query(value = "SELECT * FROM holiday_data")
    fun getHolidayEntities(): Flow<List<HolidayEntity>>

    @Query(value = "DELETE FROM holiday_data")
    suspend fun clearHolidayData()

    /**
     * Inserts [holidayEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreHolidays(holidayEntities: List<HolidayEntity>): List<Long>

    @Transaction
    suspend fun clearAndInsertHolidays(holidayEntities: List<HolidayEntity>): List<Long> {
        clearHolidayData()
        return insertOrIgnoreHolidays(holidayEntities)
    }

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertHolidays(entities: List<HolidayEntity>)
}
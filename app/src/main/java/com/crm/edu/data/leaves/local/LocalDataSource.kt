package com.crm.edu.data.leaves.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val leavesDao: LeavesDao
) {
    fun getHolidayEntities(): Flow<List<LeaveEntity>> = leavesDao.getHolidayEntities()

    suspend fun insertHolidayEntities(holidayEntities: List<LeaveEntity>) = leavesDao.insertOrIgnoreHolidays(holidayEntities)
}
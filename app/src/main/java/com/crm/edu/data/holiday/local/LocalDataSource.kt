package com.crm.edu.data.holiday.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val holidayDao: HolidayDao
) {
    fun getHolidayEntities(): Flow<List<HolidayEntity>> = holidayDao.getHolidayEntities()

    suspend fun insertHolidayEntities(holidayEntities: List<HolidayEntity>) = holidayDao.clearAndInsertHolidays(holidayEntities)
}
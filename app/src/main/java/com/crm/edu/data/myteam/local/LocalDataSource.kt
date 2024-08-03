package com.crm.edu.data.myteam.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val myTeamDao: MyTeamDao
) {
    fun getHolidayEntities(): Flow<List<HolidayEntity>> = myTeamDao.getHolidayEntities()

    suspend fun insertHolidayEntities(holidayEntities: List<HolidayEntity>) = myTeamDao.insertOrIgnoreHolidays(holidayEntities)
}
package com.crm.edu.data.myteam.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val myTeamDao: MyTeamDao
) {
    fun getStaffAttendance(): Flow<List<StaffAttendanceEntity>> = myTeamDao.getStaffAttendanceData()

    suspend fun insertHolidayEntities(staffAttendanceEntities: List<StaffAttendanceEntity>) =
        myTeamDao.insertOrIgnoreStaffAttendanceData(staffAttendanceEntities)
}
package com.crm.edu.data.leaves.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val leavesDao: LeavesDao
) {
    fun getStaffLeaveData(): Flow<List<LeaveEntity>> = leavesDao.getStaffLeaveData()

    suspend fun insertStaffLeaveData(holidayEntities: List<LeaveEntity>) = leavesDao.clearAndInsertStaffLeaveData(holidayEntities)
}
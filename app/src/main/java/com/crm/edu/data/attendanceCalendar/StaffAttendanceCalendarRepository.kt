package com.crm.edu.data.attendanceCalendar

import com.crm.edu.core.EResult
import com.crm.edu.data.myteam.StaffAttendanceData
import com.crm.edu.data.myteam.asData
import com.crm.edu.data.myteam.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class StaffAttendanceCalendarRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    fun getStaffAttendanceCalendarData(
        month: String,
        year: String,
        staffId: String?
    ): Flow<EResult<List<StaffAttendanceData>>> = flow {
        emit(EResult.Loading)
        try {
            val remoteData = if (staffId.isNullOrBlank()) remoteDataSource.getMyAttendanceCalendar(
                month,
                year
            ) else remoteDataSource.getTeamAttendanceCalendar(month, year, staffId)
            val staffAttendanceData = remoteData.data.map { it.asData() }
            emit(EResult.Success(staffAttendanceData))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }


}
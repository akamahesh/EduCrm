package com.crm.edu.data.myteam

import com.crm.edu.core.EResult
import com.crm.edu.data.myteam.local.LocalDataSource
import com.crm.edu.data.myteam.local.StaffAttendanceEntity
import com.crm.edu.data.myteam.local.asExternalModel
import com.crm.edu.data.myteam.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MyTeamRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    fun getTeamAttendance(
        month: String,
        year: String,
        teamStatus: String
    ): Flow<EResult<List<StaffAttendanceData>>> = flow {
        emit(EResult.Loading)
        val localData = localDataSource.getStaffAttendance().firstOrNull() ?: emptyList()
        emit(EResult.SuccessAndLoading(localData.map(StaffAttendanceEntity::asExternalModel)))
        try {
            val remoteData = remoteDataSource.getTeamAttendance(month, year, teamStatus)
            val staffAttendanceEntities = remoteData.data.map {
                it.asEntity()
            }
            localDataSource.insertHolidayEntities(staffAttendanceEntities)
            emit(EResult.Success(staffAttendanceEntities.map(StaffAttendanceEntity::asExternalModel)))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }


}
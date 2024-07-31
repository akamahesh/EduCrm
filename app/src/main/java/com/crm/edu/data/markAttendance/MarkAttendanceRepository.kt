package com.crm.edu.data.markAttendance

import com.crm.edu.core.EResult
import com.crm.edu.data.markAttendance.remote.MarkAttendanceData
import com.crm.edu.data.markAttendance.remote.RemoteDataSource
import com.crm.edu.data.markAttendance.remote.asData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MarkAttendanceRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    fun getCheckAttendanceData(): Flow<EResult<CheckAttendanceData>> = flow {
        emit(EResult.Loading)
        try {
            val remoteData = remoteDataSource.getCheckAttendanceData()
            val attendanceData = remoteData.asData()
            emit(EResult.Success(attendanceData))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }

    fun markCheckInOut(attendanceId: String): Flow<EResult<MarkAttendanceData>> = flow {
        emit(EResult.Loading)
        try {
            val remoteData = remoteDataSource.markCheckInOut(attendanceId)
            val attendanceData = remoteData.asData()
            emit(EResult.Success(attendanceData))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }

}
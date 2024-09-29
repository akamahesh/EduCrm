package com.crm.edu.data.leaves

import com.crm.edu.core.EResult
import com.crm.edu.data.leaves.local.LocalDataSource
import com.crm.edu.data.leaves.remote.RemoteDataSource
import com.crm.edu.data.leaves.remote.asData
import com.crm.edu.data.leaves.remote.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class LeaveRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    fun getLeaveData(teamStatus: String, month: Int, year: Int): Flow<EResult<List<LeaveData>>> =
        flow {
        emit(EResult.Loading)
        try {
            val remoteData = remoteDataSource.getLeavesData(teamStatus, month, year)
            emit(EResult.Success(remoteData.data.map { it.asExternalModel() }))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }

    fun approveLeave(
        leaveId: String,
        approvalStatus: String,
        message: String
    ): Flow<EResult<ApproveLeaveData>> = flow {
        emit(EResult.Loading)
        try {
            val remoteData =
                remoteDataSource.approveLeave(leaveId, approvalStatus, message)
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
    fun deleteLeave(
        leaveId: String,
        status: String,
    ): Flow<EResult<ApproveLeaveData>> = flow {
        emit(EResult.Loading)
        try {
            val remoteData =
                remoteDataSource.deleteLeave(leaveId, status)
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
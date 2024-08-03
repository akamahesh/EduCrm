package com.crm.edu.data.leaves

import com.crm.edu.core.EResult
import com.crm.edu.data.leaverequest.remote.LeaveRequestDetailDTO
import com.crm.edu.data.leaverequest.remote.LeaveRequestResponseDTO
import com.crm.edu.data.leaverequest.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class LeaveRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {


    fun getLeaveDetails(): Flow<EResult<LeaveRequestDetailDTO>> = flow {
        emit(EResult.Loading)
        try {
            val remoteData = remoteDataSource.fetchLeaveDetail()
            emit(EResult.Success(remoteData))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }

    fun applyLeaveRequest(
        leaveType: String,
        leaveCount: String,
        applyDates: String,
    ): Flow<EResult<LeaveRequestResponseDTO>> = flow {
        emit(EResult.Loading)
        try {
            val remoteData = remoteDataSource.applyLeaveRequest(leaveType, leaveCount, applyDates)
            emit(EResult.Success(remoteData))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))

    }

}
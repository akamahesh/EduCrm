package com.crm.edu.data.leaves

import com.crm.edu.core.EResult
import com.crm.edu.data.leaves.local.LeaveEntity
import com.crm.edu.data.leaves.local.LocalDataSource
import com.crm.edu.data.leaves.local.asExternalModel
import com.crm.edu.data.leaves.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class LeaveRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    fun getLeaveData(): Flow<EResult<List<LeaveData>>> = flow {
        emit(EResult.Loading)
        val localData = localDataSource.getStaffLeaveData().firstOrNull() ?: emptyList()
        emit(EResult.SuccessAndLoading(localData.map(LeaveEntity::asExternalModel)))
        try {
            val remoteData = remoteDataSource.getLeavesData()
            val leaveDataEntities = remoteData.data.map {
                it.asEntity()
            }
            localDataSource.insertStaffLeaveData(leaveDataEntities)
            emit(EResult.Success(leaveDataEntities.map(LeaveEntity::asExternalModel)))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }

}
package com.crm.edu.data.leaves.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val leavesApi: LeavesApi
) {

    suspend fun getLeavesData() = leavesApi.getLeavesData()

    suspend fun getLeavesData(teamStatus: String, month: Int, year: Int): LeavesResponseDTO {
        val requestBody = mapOf(
            "team_status" to teamStatus,
            "month" to month.toString(),
            "year" to year.toString(),
        )
        return leavesApi.getLeavesData(requestBody)
    }

    suspend fun approveLeave(
        leaveId: String,
        approvalStatus: String,
        message: String
    ): ApproveLeaveResponseDTO {
        val requestBody = mapOf(
            "id" to leaveId,
            "approval_status" to approvalStatus,
            "message" to message,
        )
        return leavesApi.approveLeave(requestBody)
    }

    suspend fun deleteLeave(
        leaveId: String,
        status: String,
    ): ApproveLeaveResponseDTO {
        val requestBody = mapOf(
            "id" to leaveId,
            "delete_status" to status
        )
        return leavesApi.deleteLeave(requestBody)
    }
}
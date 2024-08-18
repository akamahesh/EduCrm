package com.crm.edu.data.leaves.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val leavesApi: LeavesApi
) {

    suspend fun getLeavesData() = leavesApi.getLeavesData()

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
}
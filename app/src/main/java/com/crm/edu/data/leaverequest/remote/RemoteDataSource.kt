package com.crm.edu.data.leaverequest.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val leaveRequestApi: LeaveRequestApi) {

    suspend fun fetchLeaveDetail() = leaveRequestApi.fetchLeaveDetail()

    suspend fun applyLeaveRequest(
        leaveType: String,
        leaveCount: String,
        applyDates: String
    ): LeaveRequestResponseDTO {
        val requestBody = mapOf<String, String?>(
            "leave_type" to leaveType,
            "leave_count" to leaveCount,
            "apply_dates" to applyDates
        )
        return leaveRequestApi.leaveRequest(requestBody)
    }

}
package com.crm.edu.data.leaverequest.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val leaveRequestApi: LeaveRequestApi) {

    suspend fun fetchLeaveDetail() = leaveRequestApi.fetchLeaveDetail()

    suspend fun applyLeaveRequest(
        leaveType: String,
        fromDate: String,
        toDate: String,
        isHalfDay: Int,
        halfDayType: Int,
    ): LeaveRequestResponseDTO {
        val requestBody = mapOf<String, String?>(
            "leave_type" to leaveType,
            "from_date" to fromDate,
            "to_date" to toDate,
            "is_half_day" to isHalfDay.toString(),
            "halfday_type" to halfDayType.toString()
        )
        return leaveRequestApi.leaveRequest(requestBody)
    }

}
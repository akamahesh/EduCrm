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
        val requestBody = mutableMapOf<String, String?>(
            "leave_type" to leaveType,
            "from_date" to fromDate,
            "to_date" to toDate,
        )
        if (isHalfDay == 1) {
            requestBody["is_half_day"] = isHalfDay.toString()
            requestBody["halfday_type"] = halfDayType.toString()
        }
        return leaveRequestApi.leaveRequest(requestBody)
    }

}
package com.crm.edu.data.leaves.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeavesResponseDTO(
    @SerialName("status") var status: Int,
    @SerialName("message") var message: String? = null,
    @SerialName("data") val data: List<LeaveDetailDTO> = emptyList()
)

@Serializable
data class LeaveDetailDTO(
    @SerialName("id") val id: String,
    @SerialName("staffid") val staffId: String,
    @SerialName("staffname") val staffName: String,
    @SerialName("leave_count") val leaveCount: String,
    @SerialName("leave_type") val leaveType: String,
    @SerialName("created_by") val createdBy: String,
    @SerialName("created_date") val createdDate: String,
    @SerialName("approval_status") val approvalStatus: String,
    @SerialName("approval_by") val approvalBy: String,
    @SerialName("approval_date") val approvalDate: String,
    @SerialName("apply_date") val applyDate: String,
    @SerialName("message") val message: String,
    @SerialName("reject_message") val rejectMessage: String,
    @SerialName("is_absent") val isAbsent: String,
    @SerialName("attendance_status") val attendanceStatus: String,
    @SerialName("reason") val reason: String,
    @SerialName("dates") val dates: String,
    @SerialName("is_half_day") val isHalfDay: String,
    @SerialName("halfday_type") val halfDayType: String,
    @SerialName("to_date") val toDate: String,
    @SerialName("from_date") val fromDate: String,
    @SerialName("leave_type_name") val leaveTypeName: String,
    @SerialName("halfday_type_name") val halfDayTypeName: String,
    @SerialName("approval_status_text") val approvalStatusText: String,
    @SerialName("approval_status_color") val approvalStatusColor: String,
)
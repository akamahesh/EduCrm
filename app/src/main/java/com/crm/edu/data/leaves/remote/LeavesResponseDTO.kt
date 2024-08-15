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
    @SerialName("staffName") val staffName: String = "StaffName",
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
)
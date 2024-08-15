package com.crm.edu.data.leaves

data class LeaveData(
    val id: String,
    val staffId: String,
    val staffName: String = "StaffName",
    val leaveCount: String,
    val leaveType: String,
    val createdBy: String,
    val createdDate: String,
    val approvalStatus: String,
    val approvalBy: String,
    val approvalDate: String,
    val applyDate: String,
    val message: String,
    val rejectMessage: String,
    val isAbsent: String,
    val attendanceStatus: String,
    val reason: String,
)
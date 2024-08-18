package com.crm.edu.data.leaves

data class LeaveData(
    val id: String,
    val staffId: String,
    val staffName: String,
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
    val dates: String,
    val isHalfDay: String,
    val halfDayType: String,
    val toDate: String,
    val fromDate: String,
    val leaveTypeName: String,
    val halfDayTypeName: String,
    val approvalStatusText: String,
    val approvalStatusColor: String
)
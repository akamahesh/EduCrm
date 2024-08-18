package com.crm.edu.data.leaves

import com.crm.edu.data.leaves.local.LeaveEntity
import com.crm.edu.data.leaves.remote.LeaveDetailDTO

fun LeaveDetailDTO.asEntity() = LeaveEntity(
    id = id,
    staffId = staffId,
    staffName = staffName,
    leaveCount = leaveCount,
    leaveType = leaveType,
    createdBy = createdBy,
    createdDate = createdDate,
    approvalStatus = approvalStatus,
    approvalBy = approvalBy,
    approvalDate = approvalDate,
    applyDate = applyDate,
    message = message,
    rejectMessage = rejectMessage,
    isAbsent = isAbsent,
    attendanceStatus = attendanceStatus,
    reason = reason,
    dates = dates,
    isHalfDay = isHalfDay,
    halfDayType = halfDayType,
    toDate = toDate,
    fromDate = fromDate,
    leaveTypeName = leaveTypeName,
    halfDayTypeName = halfDayTypeName,
    approvalStatusText = approvalStatusText,
    approvalStatusColor = approvalStatusColor
)
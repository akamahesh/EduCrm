package com.crm.edu.data.leaves.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crm.edu.data.leaves.LeaveData

@Entity(tableName = "leaves_data")
data class LeaveEntity(
    @ColumnInfo(defaultValue = "")
    val id: String,
    @ColumnInfo(defaultValue = "")
    val staffId: String,
    @ColumnInfo(defaultValue = "")
    val staffName: String,
    @ColumnInfo(defaultValue = "")
    val leaveCount: String,
    @ColumnInfo(defaultValue = "")
    val leaveType: String,
    @ColumnInfo(defaultValue = "")
    val createdBy: String,
    @ColumnInfo(defaultValue = "")
    val createdDate: String,
    @ColumnInfo(defaultValue = "")
    val approvalStatus: String,
    @ColumnInfo(defaultValue = "")
    val approvalBy: String,
    @ColumnInfo(defaultValue = "")
    val approvalDate: String,
    @ColumnInfo(defaultValue = "")
    val applyDate: String,
    @ColumnInfo(defaultValue = "")
    val message: String,
    @ColumnInfo(defaultValue = "")
    val rejectMessage: String,
    @ColumnInfo(defaultValue = "")
    val isAbsent: String,
    @ColumnInfo(defaultValue = "")
    val attendanceStatus: String,
    @ColumnInfo(defaultValue = "")
    val reason: String,
    @ColumnInfo(defaultValue = "")
    val dates: String,
    @ColumnInfo(defaultValue = "")
    val isHalfDay: String,
    @ColumnInfo(defaultValue = "")
    val halfDayType: String,
    @ColumnInfo(defaultValue = "")
    val halfDayTypeName: String,
    @ColumnInfo(defaultValue = "")
    val toDate: String,
    @ColumnInfo(defaultValue = "")
    val fromDate: String,
    @ColumnInfo(defaultValue = "")
    val leaveTypeName: String,
) {
    @PrimaryKey(autoGenerate = true)
    var autoId: Long = 0
}

fun LeaveEntity.asExternalModel() = LeaveData(
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
    halfDayTypeName = halfDayTypeName,
    toDate = toDate,
    fromDate = fromDate,
    leaveTypeName = leaveTypeName
)

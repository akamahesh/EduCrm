package com.crm.edu.data.myteam.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crm.edu.data.myteam.StaffAttendanceData

@Entity(tableName = "staff_attendance_data")
data class StaffAttendanceEntity(
    @ColumnInfo(defaultValue = "")
    val time: String,
    @ColumnInfo(defaultValue = "")
    val title: String,
    @ColumnInfo(defaultValue = "")
    val start: String,
    @ColumnInfo(defaultValue = "")
    val colour: String,
    @ColumnInfo(defaultValue = "")
    val designation: String,
    @ColumnInfo(defaultValue = "")
    val staff_name: String,
) {
    @PrimaryKey(autoGenerate = true)
    var autoId: Long = 0
}

fun StaffAttendanceEntity.asExternalModel() = StaffAttendanceData(
    time = time,
    title = title,
    start = start,
    colour = colour,
    designation = designation,
    staffName = staff_name
)

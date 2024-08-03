package com.crm.edu.data.leaves.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crm.edu.data.holiday.HolidayData

@Entity(tableName = "leaves_data")
data class LeaveEntity(
    @ColumnInfo(defaultValue = "")
    val holidayName: String,
    @ColumnInfo(defaultValue = "")
    val colour: String,
    @ColumnInfo(defaultValue = "")
    val holidayDate: String,
    @ColumnInfo(defaultValue = "")
    val weekDay: String,
    @ColumnInfo(defaultValue = "")
    val holidayType: String,
) {
    @PrimaryKey(autoGenerate = true)
    var autoId: Long = 0
}

fun LeaveEntity.asExternalModel() = HolidayData(
    holidayName = holidayName,
    colour = colour,
    holidayDate = holidayDate,
    weekDay = weekDay,
    holidayType = holidayType
)

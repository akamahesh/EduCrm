package com.crm.edu.data.holiday.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crm.edu.data.holiday.HolidayData

@Entity(tableName = "holiday_data")
data class HolidayEntity(
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

fun HolidayEntity.asExternalModel() = HolidayData(
    holidayName = holidayName,
    colour = colour,
    holidayDate = holidayDate,
    weekDay = weekDay,
    holidayType = holidayType
)

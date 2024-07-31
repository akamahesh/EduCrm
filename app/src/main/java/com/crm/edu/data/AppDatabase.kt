package com.crm.edu.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crm.edu.data.holiday.local.HolidayDao
import com.crm.edu.data.holiday.local.HolidayEntity

@Database(entities = [HolidayEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
}

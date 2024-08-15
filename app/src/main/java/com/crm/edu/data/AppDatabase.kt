package com.crm.edu.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crm.edu.data.holiday.local.HolidayDao
import com.crm.edu.data.holiday.local.HolidayEntity
import com.crm.edu.data.leaves.local.LeaveEntity
import com.crm.edu.data.leaves.local.LeavesDao
import com.crm.edu.data.myteam.local.MyTeamDao
import com.crm.edu.data.myteam.local.StaffAttendanceEntity

@Database(entities = [HolidayEntity::class, StaffAttendanceEntity::class, LeaveEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
    abstract fun myTeamDao(): MyTeamDao
    abstract fun leaveDao(): LeavesDao
}

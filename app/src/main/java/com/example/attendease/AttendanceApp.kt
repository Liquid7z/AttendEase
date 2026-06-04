package com.example.attendease

import android.app.Application
import androidx.room.Room
import com.example.attendease.data.db.AttendanceDatabase
import com.example.attendease.repository.AttendanceRepository

class AttendanceApp : Application() {

    lateinit var database: AttendanceDatabase
    lateinit var repository: AttendanceRepository

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AttendanceDatabase::class.java,
            "attendance_db"
        ).build()

        repository =
            AttendanceRepository(
                database.subjectDao()
            )
    }
}
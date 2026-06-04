package com.example.attendease.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.attendease.data.dao.SubjectDao
import com.example.attendease.data.entity.Subject

@Database(
    entities = [Subject::class],
    version = 1,
    exportSchema = false
)
abstract class AttendanceDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
}
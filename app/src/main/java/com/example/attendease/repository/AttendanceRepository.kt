package com.example.attendease.repository

import com.example.attendease.data.dao.SubjectDao
import com.example.attendease.data.entity.Subject

class AttendanceRepository(
    private val dao: SubjectDao
) {

    val subjects = dao.getAllSubjects()

    suspend fun insert(subject: Subject) {
        dao.insert(subject)
    }

    suspend fun update(subject: Subject) {
        dao.update(subject)
    }

    suspend fun delete(subject: Subject) {
        dao.delete(subject)
    }
}
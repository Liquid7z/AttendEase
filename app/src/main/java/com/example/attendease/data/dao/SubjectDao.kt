package com.example.attendease.data.dao

import androidx.room.*
import com.example.attendease.data.entity.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Insert
    suspend fun insert(subject: Subject)

    @Update
    suspend fun update(subject: Subject)

    @Delete
    suspend fun delete(subject: Subject)

    @Query("SELECT * FROM subjects ORDER BY name")
    fun getAllSubjects(): Flow<List<Subject>>
}
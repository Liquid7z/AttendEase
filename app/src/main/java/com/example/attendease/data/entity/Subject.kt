package com.example.attendease.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val conducted: Int,

    val attended: Int
)
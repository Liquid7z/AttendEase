package com.example.attendease.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.attendease.data.entity.Subject
import com.example.attendease.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {

    val subjects = repository.subjects.asLiveData()

    fun insert(subject: Subject) {
        viewModelScope.launch {
            repository.insert(subject)
        }
    }

    fun update(subject: Subject) {
        viewModelScope.launch {
            repository.update(subject)
        }
    }

    fun delete(subject: Subject) {
        viewModelScope.launch {
            repository.delete(subject)
        }
    }
    fun getTotalConducted(list: List<Subject>): Int {
        return list.sumOf { it.conducted }
    }

    fun getTotalAttended(list: List<Subject>): Int {
        return list.sumOf { it.attended }
    }

    fun getAttendancePercentage(list: List<Subject>): Float {

        val conducted = getTotalConducted(list)
        val attended = getTotalAttended(list)

        if (conducted == 0) return 0f

        return (attended.toFloat() / conducted) * 100f
    }
}


class AttendanceViewModelFactory(
    private val repository: AttendanceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                AttendanceViewModel::class.java
            )
        ) {
            return AttendanceViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}
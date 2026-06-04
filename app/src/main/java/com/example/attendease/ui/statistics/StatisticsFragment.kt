package com.example.attendease.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.attendease.AttendanceApp
import com.example.attendease.R
import com.example.attendease.viewmodel.AttendanceViewModel
import com.example.attendease.viewmodel.AttendanceViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class StatisticsFragment :
    Fragment(R.layout.fragment_statistics) {

    private lateinit var viewModel: AttendanceViewModel

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val pieChart =
            view.findViewById<PieChart>(R.id.pieChart)

        val tvOverallAttendance =
            view.findViewById<TextView>(
                R.id.tvOverallAttendance
            )

        val tvTotalClasses =
            view.findViewById<TextView>(
                R.id.tvTotalClasses
            )

        val tvTotalAttended =
            view.findViewById<TextView>(
                R.id.tvTotalAttended
            )

        val tvTotalMissed =
            view.findViewById<TextView>(
                R.id.tvTotalMissed
            )

        val repository =
            (requireActivity().application as AttendanceApp)
                .repository

        val factory =
            AttendanceViewModelFactory(repository)

        viewModel =
            ViewModelProvider(
                this,
                factory
            )[AttendanceViewModel::class.java]

        viewModel.subjects.observe(
            viewLifecycleOwner
        ) { subjects ->

            val conducted =
                subjects.sumOf { it.conducted }

            val attended =
                subjects.sumOf { it.attended }

            val missed =
                conducted - attended

            val percentage =
                viewModel.getAttendancePercentage(subjects)

            val entries = arrayListOf(
                PieEntry(attended.toFloat(), "Attended"),
                PieEntry(missed.toFloat(), "Missed")
            )

            val dataSet =
                PieDataSet(entries, "Attendance")

            dataSet.sliceSpace = 3f
            dataSet.selectionShift = 5f

            dataSet.colors = listOf(
                Color.parseColor("#4CAF50"),
                Color.parseColor("#F44336")
            )

            val data = PieData(dataSet)

            pieChart.data = data
            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.legend.isEnabled = true
            pieChart.setHoleColor(Color.TRANSPARENT)
            pieChart.centerText =
                "${percentage.toInt()}%"

            tvOverallAttendance.text =
                "Overall Attendance: ${percentage.toInt()}%"

            tvTotalClasses.text =
                "Total Classes: $conducted"

            tvTotalAttended.text =
                "Attended Classes: $attended"

            tvTotalMissed.text =
                "Missed Classes: $missed"

            pieChart.animateY(1000)
            pieChart.invalidate()
        }
    }
}
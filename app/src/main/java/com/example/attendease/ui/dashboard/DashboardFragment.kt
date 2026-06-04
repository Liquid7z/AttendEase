package com.example.attendease.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.attendease.AttendanceApp
import com.example.attendease.R
import com.example.attendease.viewmodel.AttendanceViewModel
import com.example.attendease.viewmodel.AttendanceViewModelFactory

class DashboardFragment :
    Fragment(R.layout.fragment_dashboard) {

    private lateinit var viewModel: AttendanceViewModel

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // About Button
        view.findViewById<ImageButton>(
            R.id.btnAbout
        ).setOnClickListener {

            findNavController().navigate(
                R.id.developerFragment
            )
        }

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

        val tvPercent =
            view.findViewById<TextView>(
                R.id.tvAttendancePercent
            )

        val tvConducted =
            view.findViewById<TextView>(
                R.id.tvConducted
            )

        val tvAttended =
            view.findViewById<TextView>(
                R.id.tvAttended
            )

        val tvStatus =
            view.findViewById<TextView>(
                R.id.tvStatus
            )

        val tvNeedClasses =
            view.findViewById<TextView>(
                R.id.tvNeedClasses
            )

        val tvSafeBunks =
            view.findViewById<TextView>(
                R.id.tvSafeBunks
            )

        viewModel.subjects.observe(
            viewLifecycleOwner
        ) { subjects ->

            val conducted =
                viewModel.getTotalConducted(subjects)

            val attended =
                viewModel.getTotalAttended(subjects)

            val percentage =
                viewModel.getAttendancePercentage(subjects)

            tvPercent.text =
                "${percentage.toInt()}%"

            tvConducted.text =
                "Conducted: $conducted"

            tvAttended.text =
                "Attended: $attended"

            when {

                percentage >= 75f -> {
                    tvStatus.text = "SAFE ✅"
                    tvStatus.setTextColor(
                        Color.parseColor("#4CAF50")
                    )
                }

                percentage >= 65f -> {
                    tvStatus.text = "WARNING ⚠️"
                    tvStatus.setTextColor(
                        Color.parseColor("#FFC107")
                    )
                }

                else -> {
                    tvStatus.text = "CRITICAL ❌"
                    tvStatus.setTextColor(
                        Color.parseColor("#F44336")
                    )
                }
            }

            // Need Classes for 75%

            val target = 75f

            var needClasses = 0

            var futureConducted = conducted
            var futureAttended = attended

            while (
                futureConducted > 0 &&
                (futureAttended * 100f /
                        futureConducted) < target
            ) {
                futureConducted++
                futureAttended++
                needClasses++
            }

            tvNeedClasses.text =
                "Need $needClasses classes for 75%"

            // Safe Bunks

            var safeBunks = 0

            var futureBunkConducted = conducted

            while (
                futureBunkConducted > 0 &&
                (attended * 100f /
                        (futureBunkConducted + 1)) >= target
            ) {
                futureBunkConducted++
                safeBunks++
            }

            tvSafeBunks.text =
                "Safe Bunks: $safeBunks"
        }
    }
}
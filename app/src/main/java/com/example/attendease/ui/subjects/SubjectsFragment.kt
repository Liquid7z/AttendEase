package com.example.attendease.ui.subjects

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendease.AttendanceApp
import com.example.attendease.R
import com.example.attendease.data.entity.Subject
import com.example.attendease.databinding.FragmentSubjectsBinding
import com.example.attendease.viewmodel.AttendanceViewModel
import com.example.attendease.viewmodel.AttendanceViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.progressindicator.CircularProgressIndicator

class SubjectsFragment :
    Fragment(R.layout.fragment_subjects) {

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SubjectAdapter
    private lateinit var viewModel: AttendanceViewModel

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSubjectsBinding.bind(view)

        adapter = SubjectAdapter(

            onPresent = { subject ->

                AlertDialog.Builder(requireContext())
                    .setTitle("✅ Mark Present")
                    .setMessage(
                        "Add one attended and one conducted class for ${subject.name}?"
                    )
                    .setPositiveButton("Yes") { _, _ ->

                        viewModel.update(
                            subject.copy(
                                conducted = subject.conducted + 1,
                                attended = subject.attended + 1
                            )
                        )
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            },

            onAbsent = { subject ->

                AlertDialog.Builder(requireContext())
                    .setTitle("❌ Mark Absent")
                    .setMessage(
                        "Add one missed class for ${subject.name}?"
                    )
                    .setPositiveButton("Yes") { _, _ ->

                        viewModel.update(
                            subject.copy(
                                conducted = subject.conducted + 1
                            )
                        )
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            },

            onDelete = { subject ->

                AlertDialog.Builder(requireContext())
                    .setTitle("⚠️ Delete Subject")
                    .setMessage(
                        "Are you sure you want to delete ${subject.name}?\n\nThis action cannot be undone."
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        viewModel.delete(subject)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            },

            onSubjectClick = { subject ->

                val sheet =
                    BottomSheetDialog(requireContext())

                val sheetView =
                    layoutInflater.inflate(
                        R.layout.bottom_sheet_subject,
                        null
                    )

                val percentage =
                    if (subject.conducted == 0)
                        0
                    else
                        (subject.attended * 100 /
                                subject.conducted)

                sheetView.findViewById<TextView>(
                    R.id.tvSubjectTitle
                ).text = subject.name

                sheetView.findViewById<TextView>(
                    R.id.tvAttendance
                ).text =
                    "Attendance: $percentage%"
                sheetView.findViewById<CircularProgressIndicator>(
                    R.id.circularAttendance
                ).progress = percentage

                sheetView.findViewById<TextView>(
                    R.id.tvConducted
                ).text =
                    "Conducted: ${subject.conducted}"

                sheetView.findViewById<TextView>(
                    R.id.tvAttended
                ).text =
                    "Attended: ${subject.attended}"

                // Classes Needed

                var need = 0

                var futureConducted =
                    subject.conducted

                var futureAttended =
                    subject.attended

                while (
                    futureConducted > 0 &&
                    (futureAttended * 100f /
                            futureConducted) < 75f
                ) {
                    futureConducted++
                    futureAttended++
                    need++
                }

                sheetView.findViewById<TextView>(
                    R.id.tvNeedClasses
                ).text =
                    "Need $need classes for 75%"

                // Safe Bunks

                var bunks = 0

                var c = subject.conducted

                while (
                    c > 0 &&
                    (subject.attended * 100f /
                            (c + 1)) >= 75f
                ) {
                    c++
                    bunks++
                }

                sheetView.findViewById<TextView>(
                    R.id.tvSafeBunks
                ).text =
                    "Safe Bunks: $bunks"
                val attend5Percentage =
                    ((subject.attended + 5).toFloat() /
                            (subject.conducted + 5)) * 100

                val miss5Percentage =
                    (subject.attended.toFloat() /
                            (subject.conducted + 5)) * 100

                sheetView.findViewById<TextView>(
                    R.id.tvAttendPrediction
                ).text =
                    "Attend next 5 → ${attend5Percentage.toInt()}%"

                sheetView.findViewById<TextView>(
                    R.id.tvMissPrediction
                ).text =
                    "Miss next 5 → ${miss5Percentage.toInt()}%"

                sheet.setContentView(sheetView)

                sheet.show()
            }

        )

        binding.recyclerSubjects.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerSubjects.adapter = adapter

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

            adapter.submitList(subjects)

            binding.recyclerSubjects.scheduleLayoutAnimation()
        }
        binding.recyclerSubjects.scheduleLayoutAnimation()
        binding.fabAddSubject.setOnClickListener {

            binding.fabAddSubject.animate()
                .rotationBy(180f)
                .setDuration(250)
                .start()

            val dialogView = layoutInflater.inflate(
                R.layout.dialog_add_subject,
                null
            )

            AlertDialog.Builder(requireContext())
                .setTitle("Add Subject")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->

                    val name =
                        dialogView.findViewById<EditText>(
                            R.id.etSubjectName
                        ).text.toString()

                    val conducted =
                        dialogView.findViewById<EditText>(
                            R.id.etConducted
                        ).text.toString()
                            .toIntOrNull() ?: 0

                    val attended =
                        dialogView.findViewById<EditText>(
                            R.id.etAttended
                        ).text.toString()
                            .toIntOrNull() ?: 0

                    val subject =
                        Subject(
                            name = name,
                            conducted = conducted,
                            attended = attended
                        )

                    viewModel.insert(subject)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
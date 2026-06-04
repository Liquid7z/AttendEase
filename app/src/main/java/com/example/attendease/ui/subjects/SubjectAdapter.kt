package com.example.attendease.ui.subjects

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendease.data.entity.Subject
import com.example.attendease.databinding.ItemSubjectBinding
import com.google.android.material.color.MaterialColors

class SubjectAdapter(
    private val onPresent: (Subject) -> Unit,
    private val onAbsent: (Subject) -> Unit,
    private val onDelete: (Subject) -> Unit,
    private val onSubjectClick: (Subject) -> Unit
)
 : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    private var subjects = emptyList<Subject>()

    inner class SubjectViewHolder(
        private val binding: ItemSubjectBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject) {

            binding.tvSubjectName.text = subject.name

            binding.tvConducted.text =
                "Conducted: ${subject.conducted}"

            binding.tvAttended.text =
                "Attended: ${subject.attended}"

            val percentage =
                if (subject.conducted == 0)
                    0f
                else
                    (subject.attended.toFloat() /
                            subject.conducted) * 100f

            binding.tvPercentage.text =
                "${percentage.toInt()}%"

            binding.progressAttendance.setProgressCompat(
                percentage.toInt(),
                true
            )

            when {
                percentage >= 75f -> {
                    binding.tvPercentage.setTextColor(Color.GREEN)
                    binding.progressAttendance.setIndicatorColor(Color.GREEN)
                }

                percentage >= 65f -> {
                    binding.tvPercentage.setTextColor(
                        Color.parseColor("#FFA500")
                    )
                    binding.progressAttendance.setIndicatorColor(
                        Color.parseColor("#FFA500")
                    )
                }

                else -> {
                    binding.tvPercentage.setTextColor(Color.RED)
                    binding.progressAttendance.setIndicatorColor(Color.RED)
                }
            }

            binding.btnPresent.setOnClickListener {
                onPresent(subject)
            }

            binding.btnAbsent.setOnClickListener {
                onAbsent(subject)
            }

            binding.btnDelete.setOnClickListener {
                onDelete(subject)
            }
            binding.root.setOnClickListener {
                onSubjectClick(subject)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectViewHolder {

        val binding =
            ItemSubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SubjectViewHolder,
        position: Int
    ) {
        holder.bind(subjects[position])
    }

    override fun getItemCount() = subjects.size

    fun submitList(list: List<Subject>) {
        subjects = list
        notifyDataSetChanged()
    }
}
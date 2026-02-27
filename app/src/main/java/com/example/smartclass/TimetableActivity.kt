package com.example.smartclass

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartclass.databinding.ActivityTimetableBinding

class TimetableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimetableBinding
    private val subjects = mutableListOf<Pair<String, String>>()

    private val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    private val timeSlots = listOf("9-10", "10-11", "11-12", "12-1", "1-2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ADD SUBJECT
        binding.btnAdd.setOnClickListener {

            val subject = binding.etSubject.text.toString().trim()
            val teacher = binding.etTeacher.text.toString().trim()

            if (subject.isEmpty() || teacher.isEmpty()) {
                Toast.makeText(this, "Enter subject & teacher", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            subjects.add(Pair(subject, teacher))

            binding.etSubject.text?.clear()
            binding.etTeacher.text?.clear()

            Toast.makeText(this, "$subject added", Toast.LENGTH_SHORT).show()
        }

        // GENERATE AI TIMETABLE
        binding.btnGenerateAI.setOnClickListener {
            generateTimetable()
        }
    }

    private fun generateTimetable() {

        if (subjects.size < 3) {
            Toast.makeText(this, "Add at least 3 subjects", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = StringBuilder()

        // HEADER ROW
        builder.append("Time,")
        builder.append(days.joinToString(","))
        builder.append("\n")

        // Create weekly schedule map
        val weeklySchedule = mutableMapOf<String, MutableList<String>>()

        for (day in days) {

            val shuffledSubjects = subjects.map { it.first }.shuffled().toMutableList()
            val daySchedule = mutableListOf<String>()

            for (time in timeSlots) {

                if (time == "12-1") {
                    // LUNCH BREAK FIXED
                    daySchedule.add("LUNCH")
                } else {

                    // Refill if finished
                    if (shuffledSubjects.isEmpty()) {
                        shuffledSubjects.addAll(subjects.map { it.first }.shuffled())
                    }

                    daySchedule.add(shuffledSubjects.removeAt(0))
                }
            }

            weeklySchedule[day] = daySchedule
        }

        // Build table rows
        for (i in timeSlots.indices) {

            builder.append("${timeSlots[i]},")

            for (dayIndex in days.indices) {

                val day = days[dayIndex]
                builder.append(weeklySchedule[day]?.get(i))

                if (dayIndex != days.lastIndex) builder.append(",")
            }

            builder.append("\n")
        }

        val finalTimetable = builder.toString()

        val intent = Intent(this, ViewTimetableActivity::class.java)
        intent.putExtra("TIMETABLE_DATA", finalTimetable)
        startActivity(intent)
    }
}
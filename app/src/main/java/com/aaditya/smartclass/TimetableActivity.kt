package com.aaditya.smartclass

import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaditya.smartclass.databinding.ActivityTimetableBinding
import java.text.SimpleDateFormat
import java.util.*

class TimetableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimetableBinding

    private val subjects = mutableListOf<Pair<String, String>>()
    private lateinit var adapter: SubjectAdapter

    private val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    private val timeSlots = listOf("9-10", "10-11", "11-12", "12-1", "1-2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSavedSubjects()

        adapter = SubjectAdapter(subjects)
        binding.rvSubjects.layoutManager = LinearLayoutManager(this)
        binding.rvSubjects.adapter = adapter

        // ===============================
        // ADD SUBJECT
        // ===============================

        binding.btnAdd.setOnClickListener {

            val subject = binding.etSubject.text.toString().trim()
            val teacher = binding.etTeacher.text.toString().trim()

            if (subject.isEmpty() || teacher.isEmpty()) {
                Toast.makeText(this, "Enter subject & teacher", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            subjects.add(Pair(subject, teacher))
            adapter.notifyItemInserted(subjects.size - 1)

            saveSubjects()

            binding.etSubject.text?.clear()
            binding.etTeacher.text?.clear()

            Toast.makeText(this, "$subject added", Toast.LENGTH_SHORT).show()
        }

        // ===============================
        // GENERATE TIMETABLE (WITH ANIMATION)
        // ===============================

        binding.btnGenerateAI.setOnClickListener {

            if (subjects.size < 3) {
                Toast.makeText(this, "Add at least 3 subjects", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.tvLoadingAI.visibility = View.VISIBLE
            binding.tvLoadingAI.text = "Analyzing Subjects..."

            val handler = Handler(mainLooper)

            handler.postDelayed({

                binding.tvLoadingAI.text = "Optimizing Schedule..."

                handler.postDelayed({

                    binding.tvLoadingAI.text = "Generating Smart Timetable..."

                    handler.postDelayed({

                        binding.tvLoadingAI.visibility = View.GONE
                        generateTimetable()

                    }, 1200)

                }, 1200)

            }, 1200)
        }

        // ===============================
        // RESET / CREATE NEW TIMETABLE
        // ===============================

        binding.btnResetTimetable.setOnClickListener {

            val prefs = getSharedPreferences("timetable_data", MODE_PRIVATE)
            prefs.edit().clear().apply()

            subjects.clear()
            adapter.notifyDataSetChanged()

            Toast.makeText(this, "You can now create a new timetable", Toast.LENGTH_SHORT).show()
        }
    }

    // ===============================
    // SAVE / LOAD SUBJECTS
    // ===============================

    private fun saveSubjects() {
        val prefs = getSharedPreferences("timetable_data", MODE_PRIVATE)
        val data = subjects.joinToString("||") { it.first + "," + it.second }
        prefs.edit().putString("subjects_list", data).apply()
    }

    private fun loadSavedSubjects() {
        val prefs = getSharedPreferences("timetable_data", MODE_PRIVATE)
        val saved = prefs.getString("subjects_list", null) ?: return

        val items = saved.split("||")
        items.forEach {
            val parts = it.split(",")
            if (parts.size == 2) {
                subjects.add(Pair(parts[0], parts[1]))
            }
        }
    }

    // ===============================
    // ADAPTER
    // ===============================

    class SubjectAdapter(private val list: MutableList<Pair<String, String>>) :
        RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

        class SubjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txt: TextView = view.findViewById(R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.simple_list_item_1, parent, false)
            return SubjectViewHolder(view)
        }

        override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
            val item = list[position]
            holder.txt.text = item.first + " - " + item.second
        }

        override fun getItemCount(): Int = list.size
    }

    // ===============================
    // AI TIMETABLE GENERATOR
    // ===============================

    private fun generateTimetable() {

        val builder = StringBuilder()

        builder.append("Time,")
        builder.append(days.joinToString(","))
        builder.append("\n")

        val weeklySchedule = mutableMapOf<String, MutableList<String>>()
        val subjectNames = subjects.map { it.first }

        for (day in days) {

            val daySchedule = mutableListOf<String>()
            val shuffled = subjectNames.shuffled().toMutableList()

            for (time in timeSlots) {

                if (time == "12-1") {
                    daySchedule.add("LUNCH")
                } else {

                    if (shuffled.isEmpty()) {
                        shuffled.addAll(subjectNames.shuffled())
                    }

                    daySchedule.add(shuffled.removeAt(0))
                }
            }

            weeklySchedule[day] = daySchedule
        }

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

        val prefs = getSharedPreferences("timetable_data", MODE_PRIVATE)

        val time = SimpleDateFormat("dd MMM yyyy  hh:mm a", Locale.getDefault())
            .format(Date())

        prefs.edit()
            .putString("saved_timetable", finalTimetable)
            .putString("last_updated", time)
            .apply()

        val intent = Intent(this, ViewTimetableActivity::class.java)
        intent.putExtra("TIMETABLE_DATA", finalTimetable)
        startActivity(intent)
    }
}
package com.example.smartclass

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartclass.databinding.ActivityViewTimetableBinding

class ViewTimetableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewTimetableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val timetableData = intent.getStringExtra("TIMETABLE_DATA")

        if (!timetableData.isNullOrEmpty()) {
            populateTable(timetableData)
        }
    }

    private fun populateTable(data: String) {

        binding.tableViewTimetable.removeAllViews()

        val rows = data.split("\n")

        for ((rowIndex, row) in rows.withIndex()) {

            if (row.isBlank()) continue

            val columns = row.split(",")

            val tableRow = TableRow(this)

            columns.forEach { cellText ->

                val textView = TextView(this)

                textView.text = cellText
                textView.setPadding(32, 24, 32, 24)
                textView.textSize = 14f
                textView.gravity = Gravity.CENTER

                if (rowIndex == 0) {
                    // HEADER STYLE
                    textView.setTypeface(null, Typeface.BOLD)
                    textView.setBackgroundColor(0xFF3F51B5.toInt())
                    textView.setTextColor(resources.getColor(android.R.color.white))
                } else {
                    textView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
                }

                tableRow.addView(textView)
            }

            binding.tableViewTimetable.addView(tableRow)
        }
    }
}
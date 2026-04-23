package com.aaditya.smartclass

import android.R
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aaditya.smartclass.databinding.ActivityViewTimetableBinding
import java.util.Calendar

class ViewTimetableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewTimetableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Try getting timetable from intent
        var timetableData = intent.getStringExtra("TIMETABLE_DATA")

        val prefs = getSharedPreferences("timetable_data", MODE_PRIVATE)

        // 2. If not coming from intent, load saved timetable
        if (timetableData.isNullOrEmpty()) {
            timetableData = prefs.getString("saved_timetable", null)
        }

        // 3. Show Last Updated
        val lastUpdated = prefs.getString("last_updated", "Not generated yet")
        binding.txtLastUpdated.text = "Last updated: $lastUpdated"

        // 4. If still empty → show message
        if (timetableData.isNullOrEmpty()) {
            Toast.makeText(this, "No timetable generated yet", Toast.LENGTH_SHORT).show()
            return
        }

        populateTable(timetableData)

        // 5. Highlight today's column
        highlightTodayColumn()
    }

    // =====================================
    // POPULATE TABLE
    // =====================================

    private fun populateTable(data: String) {

        binding.tableViewTimetable.removeAllViews()

        val rows = data.split("\n")

        for ((rowIndex, row) in rows.withIndex()) {

            if (row.isBlank()) continue

            val columns = row.split(",")

            val tableRow = TableRow(this)

            columns.forEachIndexed { columnIndex, cellText ->

                val textView = TextView(this)

                textView.text = cellText
                textView.setPadding(28, 22, 28, 22)
                textView.textSize = 14f
                textView.gravity = Gravity.CENTER

                if (rowIndex == 0) {
                    // HEADER STYLE
                    textView.setTypeface(null, Typeface.BOLD)
                    textView.setBackgroundColor(0xFF3F51B5.toInt())
                    textView.setTextColor(resources.getColor(R.color.white))
                    textView.textSize = 15f
                } else {
                    // NORMAL CELL STYLE
                    textView.setBackgroundColor(0xFFF9FAFB.toInt())
                    textView.setTextColor(0xFF111827.toInt())
                }

                tableRow.addView(textView)
            }

            binding.tableViewTimetable.addView(tableRow)
        }
    }

    // =====================================
    // HIGHLIGHT TODAY COLUMN
    // =====================================

    private fun highlightTodayColumn() {

        val todayColumn = getTodayColumnIndex()

        if (todayColumn == -1) return

        val rowCount = binding.tableViewTimetable.childCount

        for (i in 0 until rowCount) {

            val row = binding.tableViewTimetable.getChildAt(i) as TableRow

            // avoid crash if row is smaller
            if (todayColumn < row.childCount) {

                val cell = row.getChildAt(todayColumn) as TextView

                // highlight color
                cell.setBackgroundColor(0xFFBBDEFB.toInt())
                cell.setTypeface(null, Typeface.BOLD)
            }
        }
    }

    private fun getTodayColumnIndex(): Int {

        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)

        return when (today) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            else -> -1
        }
    }
}
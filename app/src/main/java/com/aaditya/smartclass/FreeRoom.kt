package com.aaditya.smartclass

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class FreeRoomActivity : AppCompatActivity() {

    private val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    private val times = listOf("9-10", "10-11", "11-12", "12-1", "1-2")

    // All classrooms in college
    private val allRooms = listOf(
        "Room 101", "Room 102", "Room 103", "Room 104", "Room 105",
        "Room 201", "Room 202", "Room 203", "Room 204", "Room 205"
    )

    // Timetable storage
    private val timetable = mutableMapOf<String, MutableMap<String, MutableList<String>>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_room)

        val spinnerDay = findViewById<Spinner>(R.id.spinnerDay)
        val spinnerTime = findViewById<Spinner>(R.id.spinnerTime)
        val btnCheck = findViewById<Button>(R.id.btnCheckRoom)
        val txtResult = findViewById<TextView>(R.id.txtResult)

        // Generate realistic timetable
        generateTimetableData()

        spinnerDay.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        spinnerTime.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, times)

        btnCheck.setOnClickListener {

            val selectedDay = spinnerDay.selectedItem.toString()
            val selectedTime = spinnerTime.selectedItem.toString()

            val occupiedRooms = timetable[selectedDay]?.get(selectedTime) ?: mutableListOf()

            val freeRooms = allRooms.filter { room -> !occupiedRooms.contains(room) }

            if (freeRooms.isEmpty()) {
                txtResult.text = "No rooms available at this time."
            } else {
                txtResult.text = formatRooms(freeRooms, selectedDay, selectedTime)
            }
        }
    }

    private fun generateTimetableData() {

        for (day in days) {

            val dayMap = mutableMapOf<String, MutableList<String>>()

            for (time in times) {

                if (time == "12-1") {
                    // Lunch break
                    dayMap[time] = mutableListOf()
                } else {

                    // Random occupied rooms
                    val occupied = allRooms.shuffled().take((2..5).random()).toMutableList()
                    dayMap[time] = occupied
                }
            }

            timetable[day] = dayMap
        }
    }

    private fun formatRooms(rooms: List<String>, day: String, time: String): String {

        val builder = StringBuilder()

        builder.append("Free Rooms on $day ($time)\n")
        builder.append("────────────────────\n\n")

        for (room in rooms) {
            builder.append("• $room\n")
        }

        builder.append("\nTotal Available: ${rooms.size}")

        return builder.toString()
    }
}
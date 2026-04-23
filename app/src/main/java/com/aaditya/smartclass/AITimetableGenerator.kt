package com.aaditya.smartclass

object AITimetableGenerator {

    fun generateTimetable(
        subjects: List<String>,
        teachers: List<String>
    ): List<TimetableModel> {

        val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday")
        val times = listOf(
            "9:00 - 10:00",
            "10:00 - 11:00",
            "11:00 - 12:00",
            "12:00 - 1:00",
            "2:00 - 3:00"
        )

        val timetable = mutableListOf<TimetableModel>()
        val usedSlots = mutableSetOf<String>()

        for (subject in subjects) {

            var assigned = false

            while (!assigned) {

                val day = days.random()
                val time = times.random()
                val teacher = teachers.random()

                val slot = "$day-$time"

                if (!usedSlots.contains(slot)) {

                    timetable.add(
                        TimetableModel(
                            subject,
                            teacher,
                            day,
                            time
                        )
                    )

                    usedSlots.add(slot)
                    assigned = true
                }
            }
        }

        return timetable
    }
}
package com.aaditya.smartclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aaditya.smartclass.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        showUserName()
        setupClickListeners()
    }

    private fun showUserName() {

        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)
        val savedName = prefs.getString("user_name", "User")

        binding.txtWelcome.text = "Welcome, $savedName"
    }

    private fun setupClickListeners() {

        // 📅 View Timetable
        binding.cardTimetable.setOnClickListener {
            startActivity(Intent(this, ViewTimetableActivity::class.java))
        }

        // 🤖 AI Timetable
        binding.cardAIScheduler.setOnClickListener {
            startActivity(Intent(this, TimetableActivity::class.java))
        }

        // 🏫 Free Rooms
        binding.cardFreeRooms.setOnClickListener {
            startActivity(Intent(this, FreeRoomActivity::class.java))
        }

        // 👤 OPEN PROFILE
        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // 🔐 Logout
        binding.btnLogout.setOnClickListener {

            auth.signOut()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            finish()
        }
    }
}
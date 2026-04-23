package com.aaditya.smartclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aaditya.smartclass.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        loadUserData()

        // ✅ OPEN EDIT PROFILE
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        // ✅ LOGOUT
        binding.btnLogout.setOnClickListener {

            auth.signOut()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            finish()
        }
    }

    // 🔥 VERY IMPORTANT → refresh when coming back from edit screen
    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {

        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)

        val name = prefs.getString("user_name", "User")
        val semester = prefs.getString("semester", "Not set")
        val section = prefs.getString("section", "Not set")
        val roll = prefs.getString("roll_no", "Not set")

        val email = auth.currentUser?.email ?: "No Email"

        // BASIC INFO
        binding.txtName.text = name
        binding.txtEmail.text = email

        // AVATAR
        val firstLetter = name?.firstOrNull()?.uppercaseChar().toString()
        binding.txtAvatar.text = firstLetter

        // 🎓 ACADEMIC INFO (MAKE SURE IDs EXIST IN XML)
        binding.txtSemester.text = "Semester: $semester"
        binding.txtSection.text = "Section: $section"
        binding.txtRoll.text = "Roll No: $roll"
    }
}
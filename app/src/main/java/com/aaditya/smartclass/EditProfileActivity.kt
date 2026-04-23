package com.aaditya.smartclass

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aaditya.smartclass.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveData()
    }

    private fun saveData() {

        binding.btnSave.setOnClickListener {

            val semester = binding.etSemester.text.toString()
            val section = binding.etSection.text.toString()
            val roll = binding.etRoll.text.toString()

            // ✅ FIX IS HERE
            val prefs = this@EditProfileActivity.getSharedPreferences("user_data", MODE_PRIVATE)

            prefs.edit()
                .putString("semester", semester)
                .putString("section", section)
                .putString("roll", roll)
                .apply()

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
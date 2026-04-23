package com.aaditya.smartclass

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aaditya.smartclass.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        registerUser()
        goToLogin()
    }

    private fun registerUser() {

        binding.btnSignup.setOnClickListener {

            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // 🔥 NEW FIELDS
            val semester = binding.etSemester.text.toString().trim()
            val section = binding.etSection.text.toString().trim()
            val roll = binding.etRoll.text.toString().trim()

            // ✅ VALIDATION
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || semester.isEmpty() || section.isEmpty() || roll.isEmpty()) {

                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        // ✅ SAVE ALL USER DATA
                        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)

                        prefs.edit()
                            .putString("user_name", name)
                            .putString("semester", semester)
                            .putString("section", section)
                            .putString("roll_no", roll)
                            .apply()

                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun goToLogin() {
        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}
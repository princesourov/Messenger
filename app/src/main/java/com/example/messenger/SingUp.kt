package com.example.messenger

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.messenger.databinding.ActivitySingUpBinding
import com.google.firebase.auth.FirebaseAuth

class SingUp : AppCompatActivity() {
    // Binding
    private lateinit var singUpBinding: ActivitySingUpBinding
    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singUpBinding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(singUpBinding.root)

        // Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // Move to LogIn
        singUpBinding.RegisterBtn.setOnClickListener {
            val email = singUpBinding.SingUpEmail.text.toString().trim()
            val password = singUpBinding.registerPassword.text.toString().trim()
            val rePassword = singUpBinding.retregiPassword.text.toString().trim()

            // Validation
            if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else if (password != rePassword) {
                Toast.makeText(applicationContext, "Confirm password did not match", Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(password)) {
                Toast.makeText(
                    applicationContext,
                    "Password must contain at least 8 characters, including uppercase, lowercase, number, and special character.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Firebase Sign Up
                signUpUser(email, password)
            }
        }

        // Move to LogIn
        singUpBinding.existinguser.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Firebase SignUp Function
    private fun signUpUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LogIn::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Password Validation Function
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isDigit() } &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { !it.isLetterOrDigit() }
    }
}

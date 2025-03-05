package com.example.messenger

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    // Binding
    lateinit var Fbinding: ActivityForgotPasswordBinding
    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fbinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = Fbinding.root
        setContentView(view)



        // Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // Forgot Password: Add this functionality
        Fbinding.IForgotPassword.setOnClickListener {
            val email = Fbinding.Email.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }
}
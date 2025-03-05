package com.example.messenger

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {
    // Binding
    lateinit var LoginBinding: ActivityLogInBinding
    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    // OnBackPressedCallback
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(LoginBinding.root)

        // Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // OnBackPressedCallback
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 3000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this@LogIn, "Press back again to leave the app.", Toast.LENGTH_LONG).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        LoginBinding.LogInBtn.setOnClickListener {
            val email = LoginBinding.LoginUserEmail.text.toString().trim()
            val password = LoginBinding.LoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("E-mail", email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Move to SignUp activity
        LoginBinding.NewUser.setOnClickListener {
            val intent = Intent(this, SingUp::class.java)
            startActivity(intent)
        }
        // Move to ForgotPassword activity
        LoginBinding.ForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }
}
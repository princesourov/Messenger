package com.example.messenger

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("Users")

        binding.addbutton.setOnClickListener {
            addUserData()
        }
    }

    private fun addUserData() {
        val name = binding.nameEt.text.toString().trim()
        val message = binding.EtSms.text.toString().trim()

        if (name.isEmpty() || message.isEmpty()) {
            Toast.makeText(applicationContext, "SMS Empty", Toast.LENGTH_SHORT).show()
            return
        }

        val id = ref.push().key ?: return
        val user = Users(id, name, message)

        ref.child(id).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show()
                clearUI()
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearUI() {
        binding.EtSms.text.clear()
        binding.EtSms.clearFocus()

        /*// Hide Keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)*/
    }
}

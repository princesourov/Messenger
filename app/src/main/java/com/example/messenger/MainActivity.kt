package com.example.messenger

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityMainBinding
    // Firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    //ArrayList
    val arraylist = ArrayList<Users>()
    //Adapter
    lateinit var adapter: UserAdapter
    // OnBackPressedCallback
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // OnBackPressedCallback
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 3000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, "Press back again to leave the app.", Toast.LENGTH_LONG).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // Data Set
        // Get username from Intent
        val EmailFromIntent = intent.getStringExtra("E-mail")

        // shoro user user name
        if (EmailFromIntent != null) {
            // Show the username on HomePage
            binding.nameEt.text = "$EmailFromIntent"
        }
        // Firebase initialization
        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("Users")

        binding.addbutton.setOnClickListener {
            addUserData()
        }
        //Get Data
        userdatagate()
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

    // Get Data from Firebase
    fun userdatagate() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arraylist.clear()
                for (userdata in snapshot.children) {
                    val user = userdata.getValue(Users::class.java)
                    if (user != null) {
                        arraylist.add(user)
                    }
                }

                // Adapter কনফিগারেশন
                if (!::adapter.isInitialized) {
                    adapter = UserAdapter(this@MainActivity, arraylist)
                    val layoutManager = LinearLayoutManager(this@MainActivity).apply {
                        reverseLayout = true  // নতুন মেসেজ উপরে আসবে
                        stackFromEnd = false   // স্ক্রল নিচ থেকে শুরু হবে, মানে উপরে থাকবে
                    }
                    binding.Recyclerview.layoutManager = layoutManager
                    binding.Recyclerview.adapter = adapter
                } else {
                    adapter.notifyDataSetChanged()  // ডাটা আপডেট হলে রিফ্রেশ হবে
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}

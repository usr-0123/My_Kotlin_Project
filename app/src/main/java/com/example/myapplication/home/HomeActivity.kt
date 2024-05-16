package com.example.myapplication.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        // Assuming you have Firebase initialized somewhere in your code
        val auth = FirebaseAuth.getInstance()

        val logout = findViewById<TextView>(R.id.logout)

        // Inside your activity or fragment
        logout.setOnClickListener {
            // Sign out the user
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
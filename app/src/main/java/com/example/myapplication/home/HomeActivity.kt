package com.example.myapplication.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.myapplication.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val profile = findViewById<ImageView>(R.id.profileImg)
        val master = findViewById<Button>(R.id.homeBtn)

        profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        master.setOnClickListener {
            val intent = Intent(this, MasterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
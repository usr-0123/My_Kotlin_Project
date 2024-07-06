package com.example.myapplication.home.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var userListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Assuming you have Firebase initialized somewhere in your code
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()

        userReference = database.getReference("users").child(auth.currentUser?.uid ?: "")

        loadUserData()

        val logout = findViewById<Button>(R.id.logout)

        // Inside your activity or fragment
        logout.setOnClickListener {
            // Sign out the user
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val back = findViewById<TextView>(R.id.profileHeader)

        back.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserData() {
        userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val userEmail = dataSnapshot.child("email").getValue(String::class.java)

                val username = dataSnapshot.child("username").getValue(String::class.java)

                val imageUrl = dataSnapshot.child("profileImageUrl").getValue(String::class.java)

                val userEmailHolder = findViewById<TextView>(R.id.email)
                val usernameHolder = findViewById<TextView>(R.id.username)
                val profileUrlHolder = findViewById<ImageView>(R.id.profileImg)

                if (!userEmail.isNullOrEmpty()) {
                    userEmailHolder.text = userEmail
                } else {
                    userEmailHolder.text = "Loading email..."
                }

                if (!username.isNullOrEmpty()) {
                    usernameHolder.text = username
                } else {
                    usernameHolder.text = "Loading username..."
                }

                if (!imageUrl.isNullOrEmpty()) {
                    Glide.with(this@ProfileActivity)
                        .load(imageUrl)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(profileUrlHolder)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        // Attach ValueEventListener to userReference
        userReference.addValueEventListener(userListener)
    }

    private fun editUsername () {
        // myView.visibility = View.GONE or View.INVISIBLE
    }
}
package com.example.myapplication.home
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMasterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MasterActivity : AppCompatActivity() {
    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var userListener: ValueEventListener

    // Other properties
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMasterBinding

    private lateinit var userEmailAddress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance()
        userReference = database.getReference("users").child(auth.currentUser?.uid ?: "lewiskipngetichkemboi@gmail.com")

        // Set up the toolbar
        setSupportActionBar(binding.appBarMaster.toolbar)

        // Set up the floating action button
        binding.appBarMaster.fab.setOnClickListener {
            val intent = Intent(this, AllUsersActivity::class.java)
            startActivity(intent)

        //        view ->
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //    .setAction("Action", null).show()

        }

        // Set up navigation drawer and navigation controller
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_master)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Load user profile image dynamically
        loadUserProfileImage()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.master, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_master)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Handle Settings option click
                openSettings()
                true
            }
            R.id.action_logout -> {
                // Handle Logout option click
                logoutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettings() {
        // Implement the functionality to open settings screen
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun logoutUser() {
        // Implement the functionality to logout the user
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadUserProfileImage() {
        // Get the reference to ImageView
        val imageView: ImageView = binding.navView.getHeaderView(0).findViewById(R.id.imageView)

        // Load user profile image URL from Firebase Database
        userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userProfileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String::class.java)

                val userEmail = dataSnapshot.child("email").getValue(String::class.java)

                if (!userEmail.isNullOrEmpty()) {
                    val userMail = findViewById<TextView>(R.id.textView)

                    userMail.text = "$userEmail"
                } else {
                    userEmailAddress = findViewById(R.id.textView)

                    userEmailAddress.text = "lewiskipngetichkemboi"
                }

                showToast("User profile image URL: $userProfileImageUrl")

                // Use Glide to load the user profile image into ImageView
                if (!userProfileImageUrl.isNullOrEmpty()) {
                    Glide.with(this@MasterActivity)
                        .load(userProfileImageUrl)
                        .placeholder(R.drawable.bet365_logo) // Placeholder image while loading
                        .error(R.drawable.bet365_logo) // Error image if loading fails
                        .into(imageView)
                } else {
                    // Handle case where user profile image URL is empty or null
                    // You can set a default image or handle it as needed
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                // You may want to log the error or show a message to the user
            }
        }

        // Attach ValueEventListener to userReference
        userReference.addValueEventListener(userListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove ValueEventListener when activity is destroyed
        userReference.removeEventListener(userListener)
    }
    private fun showToast(message: String) {
        Toast.makeText(this@MasterActivity, message, Toast.LENGTH_SHORT).show()
    }
}
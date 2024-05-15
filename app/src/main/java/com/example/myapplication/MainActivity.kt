package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.home.UsersActivity

class MainActivity : AppCompatActivity() {

    private val TIMEOUT_MS = 3000L // 3 seconds timeout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Delayed navigation to next page after TIMEOUT_MS
        Handler().postDelayed({
            navigateToNextPage()
        }, TIMEOUT_MS)
    }

    private fun navigateToNextPage() {
        val intent = Intent(this, UsersActivity::class.java)
        startActivity(intent)
        finish() // Finish current activity to prevent going back to it
    }
}

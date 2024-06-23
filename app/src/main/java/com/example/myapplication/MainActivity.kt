package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.home.authentication.SignInActivity
import com.example.myapplication.home.states.NetworkUtil

class MainActivity : AppCompatActivity() {

    private val TIMEOUT_MS = 4000L // 3 seconds timeout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Delayed navigation to next page after TIMEOUT_MS
        Handler().postDelayed({
            navigateToNextPage()
        }, TIMEOUT_MS)

        // Network state
        if (!NetworkUtil.isConnected(this)) {
            showOfflineError()
        }
    }

    private fun navigateToNextPage() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish() // Finish current activity to prevent going back to it
    }

    private fun showOfflineError() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton(
                "OK"
            ) { dialog, _ -> dialog.dismiss() }
            .setIcon(R.drawable.logo)
            .show()
    }
}

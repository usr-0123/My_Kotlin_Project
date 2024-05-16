package com.example.myapplication.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var editPasswordText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.editTextEmail)
        editPasswordText = findViewById(R.id.editTextPassword)
        submitButton = findViewById(R.id.buttonSignIn)

        val signup = findViewById<TextView>(R.id.signupNavigate)

        signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = editPasswordText.text.toString().trim()

            if (email.isEmpty() || email.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please enter your details.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Change button text to "Loading..."
            submitButton.text = "Loading..."

            // Disable button to prevent multiple clicks during registration process
            submitButton.isEnabled = false

            loginUser(email, password)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent the user from going back to the sign-in screen
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        // User is logged in and email is verified
                        // Proceed to your app's main screen or perform other actions
                        updateUI(user)
                        submitButton.isEnabled = true
                        submitButton.text = "Login"
                    } else {
                        // User is logged in but email is not verified
                        // Prevent login and prompt the user to verify their email
                        Toast.makeText(
                            applicationContext,
                            "Please verify your email before logging in.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // You can optionally sign out the user to force them to verify their email before logging in again
                        auth.signOut()
                        submitButton.isEnabled = true
                        submitButton.text = "Login"
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                    submitButton.isEnabled = true
                    submitButton.text = "Login"
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null && user.isEmailVerified) {
            Toast.makeText(
                applicationContext,
                "Welcome",
                Toast.LENGTH_SHORT
            ).show()

            // Example: Navigate to the LoginActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            return
        }
    }
}
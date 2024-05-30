package com.example.myapplication.home.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var editPasswordText: EditText
    private lateinit var editConfirmPassword: EditText
    private lateinit var passwordMismatch: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Find views
        emailEditText = findViewById(R.id.editTextEmail)
        submitButton = findViewById(R.id.buttonRegister)
        editPasswordText = findViewById(R.id.editTextPassword)
        editConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        passwordMismatch = findViewById(R.id.passwordMismatchTextView)

        val signInNavigate = findViewById<TextView>(R.id.signInNavigate)

        signInNavigate.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = editPasswordText.text.toString().trim()
            val confirmPassword = editConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please enter your Details.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                passwordMismatch.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // Change button text to "Loading..."
            submitButton.text = "Loading..."

            // Disable button to prevent multiple clicks during registration process
            submitButton.isEnabled = false

            // Register user with email and password
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User registration successful, email verification link sent
                    val user = auth.currentUser
                    saveUserEmailToFirestore(email)
                    sendEmailVerification(user)
                    updateUI(user)
                    submitButton.isEnabled = true
                    submitButton.text = "Register"
                } else {
                    // User registration failed
                    Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    submitButton.isEnabled = true
                    submitButton.text = "Register"
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SignUpActivity", "Email verification sent to ${user.email}")
                } else {
                    Log.e("SignUpActivity", "Error sending email verification", task.exception)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(
                applicationContext,
                "Please check your email for verification.",
                Toast.LENGTH_SHORT
            ).show()
            auth.signOut()
            // Example: Navigate to the LoginActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            return
        }
    }

    private fun saveUserEmailToFirestore(email: String) {
        // Access Firestore instance
        val firestore = FirebaseFirestore.getInstance()

        // Create a new document with the user's email as the document ID
        firestore.collection("users").document(email)
            .set(mapOf("email" to email))
            .addOnSuccessListener {
                Log.d("SignUpActivity", "User email saved to Firestore: $email")
            }
            .addOnFailureListener { e ->
                Log.e("SignUpActivity", "Error saving user email to Firestore", e)
            }
    }
}

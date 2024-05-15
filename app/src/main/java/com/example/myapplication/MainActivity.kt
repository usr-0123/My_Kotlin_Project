package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.home.UsersActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set onClickListener for the TextView to navigate to the next activity
        val button : Button = findViewById(R.id.buttonNavigate)
        val textView : TextView = findViewById(R.id.textView)
        button.setOnClickListener() {
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }
        textView.setOnClickListener(){Toast.makeText(this,"TextView Pressed", Toast.LENGTH_SHORT).show()}
    }
}

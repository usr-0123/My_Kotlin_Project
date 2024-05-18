package com.example.myapplication.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var profileSettings : TextView
    private lateinit var notificationSettings : TextView
    private lateinit var notificationsSwitch : Switch
    private lateinit var appearanceSettings : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        profileSettings = findViewById(R.id.profileSettings)

        appearanceSettings = findViewById(R.id.appearanceSettings)

        // Set up a click listener for the theme options button
        appearanceSettings.setOnClickListener { view ->
            showThemeOptionsPopup(view)
        }

        profileSettings.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showThemeOptionsPopup(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_theme_options, popupMenu.menu)

        // Set an item click listener for the popup menu
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_dark_mode -> {
                    // Set app theme to dark mode
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    true
                }
                R.id.menu_light_mode -> {
                    // Set app theme to light mode
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    true
                }
                R.id.menu_system_default -> {
                    // Set app theme to system default (follow system settings)
                    setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    true
                }
                else -> false
            }
        }

        // Show the popup menu
        popupMenu.show()
    }
}
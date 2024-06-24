package com.example.myapplication.home.ui.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class ProfilePageActivity extends AppCompatActivity {

//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.ImageButton;
//        import android.widget.ImageView;
//        import android.widget.TextView;
//        import android.widget.Toast;
//        import androidx.annotation.Nullable;
//        import androidx.appcompat.app.AppCompatActivity;

    private ImageView profileImage;
    private TextView username;
    private TextView email;
    private ImageButton editProfileImage;
    private ImageButton editUsername;
    private ImageButton editEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Initialize views
        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        editProfileImage = findViewById(R.id.edit_profile_image);
        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);

        // Set onClick listeners
        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit profile image action
                Toast.makeText(ProfilePageActivity.this, "Edit Profile Image clicked", Toast.LENGTH_SHORT).show();
            }
        });

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit username action
                Toast.makeText(ProfilePageActivity.this, "Edit Username clicked", Toast.LENGTH_SHORT).show();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit email action
                Toast.makeText(ProfilePageActivity.this, "Edit Email clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

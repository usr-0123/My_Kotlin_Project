package com.example.myapplication.home.ui.gallery.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.myapplication.R;

public class GroupActivity extends AppCompatActivity {

    private  String groupName;

    private TextView groupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupName = getIntent().getStringExtra("groupTitle");
        groupTitle = findViewById(R.id.groupTitle);
        groupTitle.setText(groupName);
    }
}
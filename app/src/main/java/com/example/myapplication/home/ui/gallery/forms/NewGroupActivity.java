package com.example.myapplication.home.ui.gallery.forms;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewGroupActivity extends AppCompatActivity {

    private EditText editTextPostMessage;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        databaseReference = FirebaseDatabase.getInstance().getReference("groups");

        editTextPostMessage = findViewById(R.id.editTextPostMessage);
        Button buttonCreateGroup = findViewById(R.id.buttonCreatePost);

        buttonCreateGroup.setOnClickListener(v -> createPost());
    }

    protected void createPost() {
        String groupName = editTextPostMessage.getText().toString().trim();

        if (groupName.isEmpty()) {
            Toast.makeText(this, "Please enter group name", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference newGroupRef = databaseReference.push();
        newGroupRef.child("groupName").setValue(groupName);
        newGroupRef.child("createdDate").setValue(System.currentTimeMillis());

        saveGroup(newGroupRef);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveGroup(DatabaseReference newGroupRef) {
        Toast.makeText(this, "Group created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
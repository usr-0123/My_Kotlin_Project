package com.example.myapplication.home.forms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Objects;

public class NewReportForm extends Activity {

    private static final int REQUEST_CODE_ATTACH_FILE = 1;

    private EditText editTextPostMessage;

    private DatabaseReference databaseReference;

    private Uri attachmentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report_form);

        databaseReference = FirebaseDatabase.getInstance().getReference("reports");

        editTextPostMessage = findViewById(R.id.editTextPostMessage);
        Button buttonAttachFile = findViewById(R.id.buttonAttachFile);

        Button buttonCreatePost = findViewById(R.id.buttonCreatePost);

        buttonAttachFile.setOnClickListener(v -> attachFile());

        buttonCreatePost.setOnClickListener(v -> createPost());
    }

    private void attachFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_ATTACH_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ATTACH_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            attachmentUri = data.getData();
            Toast.makeText(this, "Attachment selected", Toast.LENGTH_SHORT).show();
            previewFile(attachmentUri);
        }
    }

    private void previewFile(Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Preview File"));
    }

    private void createPost() {
        String postMessage = editTextPostMessage.getText().toString().trim();

        if (postMessage.isEmpty()) {
            Toast.makeText(this, "Please enter a post message", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference newPostRef = databaseReference.push();
        newPostRef.child("message").setValue(postMessage);
        newPostRef.child("userEmail").setValue("user@example.com");
        newPostRef.child("datetime").setValue(System.currentTimeMillis());

        if (attachmentUri != null) {
            uploadAttachment(attachmentUri, newPostRef);
            showToast("Attachment uri available boss");
        } else {
            savePost(newPostRef);
            showToast("Saved without attachment");
        }
    }

    private void uploadAttachment(Uri attachmentUri, final DatabaseReference newPostRef) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Get file extension
        String fileExtension = getFileExtension(attachmentUri);
        String folderName = getFolderName(fileExtension);

        StorageReference fileRef = storageRef.child(folderName).child(Objects.requireNonNull(attachmentUri.getLastPathSegment()));

        fileRef.putFile(attachmentUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get download URL
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save URL to Firestore
                        newPostRef.child("attachmentURL").setValue(uri.toString());
                        savePost(newPostRef);
                        // Show success message
                        showToast("Attachment uploaded successfully");
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NewReportForm.this, "Failed to upload attachment", Toast.LENGTH_SHORT).show();
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri) {
        return Objects.requireNonNull(getContentResolver().getType(uri)).split("/")[1];
    }

    private String getFolderName(String fileExtension) {
        switch (fileExtension.toLowerCase()) {
            case "jpg":
            case "jpeg":
            case "png":
                return "images";
            case "mp4":
            case "avi":
            case "mov":
                return "videos";
            case "pdf":
            case "doc":
            case "docx":
                return "documents";
            default:
                return "others";
        }
    }

    private void savePost(DatabaseReference newPostRef) {
        Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
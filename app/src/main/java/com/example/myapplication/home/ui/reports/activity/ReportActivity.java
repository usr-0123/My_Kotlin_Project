package com.example.myapplication.home.ui.reports.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.ui.reports.adapters.ImagesAdapter;
import com.example.myapplication.home.ui.reports.model.Reply;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private TextView reportMessage;
    private RecyclerView imagesRecyclerView;
    private ImagesAdapter imagesAdapter;
    private DatabaseReference databaseReference;
    private EditText replyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ImageView sendButton = findViewById(R.id.sendBtn);

        sendButton.setOnClickListener(v -> sendMessage());

        String reportName = getIntent().getStringExtra("reportName");
        String reportId = getIntent().getStringExtra("reportId");
        TextView reportTitle = findViewById(R.id.groupTitle);
        reportMessage = findViewById(R.id.reportMessage);
        imagesRecyclerView = findViewById(R.id.imagesRecyclerView);

        replyEditText = findViewById(R.id.commentInput);

        reportTitle.setText(reportName);

        databaseReference = FirebaseDatabase.getInstance().getReference("reports").child(reportId);
        fetchReportData();

        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imagesAdapter = new ImagesAdapter(this, new ArrayList<>());
        imagesRecyclerView.setAdapter(imagesAdapter);
    }

    private void sendMessage() {
    // Send messages
        String reportId = getIntent().getStringExtra("reportId");
        Toast.makeText(this, reportId, Toast.LENGTH_SHORT).show();
        DatabaseReference replyReference = FirebaseDatabase.getInstance("reports").getReference().child(reportId);
        String replyText = replyEditText.getText().toString().trim();

        long timestamp = System.currentTimeMillis();
        if (!replyText.isEmpty()) {
            Reply reply = new Reply("userId", replyText, timestamp);
            replyReference.push().setValue(reply);
            replyEditText.setText("");
        }
        Toast.makeText(this, "Replied!", Toast.LENGTH_SHORT).show();
    }

    private void fetchReportData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String message = dataSnapshot.child("message").getValue(String.class);
                    reportMessage.setText(message);

                    List<String> imageUrls = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("attachmentURLs").getChildren()) {
                        String imageUrl = snapshot.getValue(String.class);
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            imageUrls.add(imageUrl);
                        }
                    }

                    imagesAdapter = new ImagesAdapter(ReportActivity.this, imageUrls);
                    imagesRecyclerView.setAdapter(imagesAdapter);
                } else {
                    reportMessage.setText("Report not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                reportMessage.setText("Failed to load report.");
            }
        });
    }
}

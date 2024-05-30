package com.example.myapplication.home.ui.reports.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportActivity extends AppCompatActivity {

    private String reportId;
    private  String reportName;
    private TextView reportTitle;

    private TextView reportMessage;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportName = getIntent().getStringExtra("reportName");

        reportId = getIntent().getStringExtra("reportId");

        reportTitle = findViewById(R.id.groupTitle);

        reportMessage = findViewById(R.id.reportMessage);

        reportTitle.setText(reportName);

        databaseReference = FirebaseDatabase.getInstance().getReference("reports").child(reportId);

        fetchReportData();
    }

    private void fetchReportData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String message = dataSnapshot.child("message").getValue(String.class);
                    reportMessage.setText(message);
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
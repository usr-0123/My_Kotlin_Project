package com.example.myapplication.home.ui.reports.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;

public class ReportActivity extends AppCompatActivity {

    private  String reportName;
    private TextView reportTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportName = getIntent().getStringExtra("reportName");

        reportTitle = findViewById(R.id.groupTitle);
        reportTitle.setText(reportName);
    }
}
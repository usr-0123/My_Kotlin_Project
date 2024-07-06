// HomeActivity.java
package com.example.myapplication.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.home.states.NetworkUtil;
import com.example.myapplication.home.ui.reports.activity.ReportActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private LinearLayout reportsContainer;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        reportsContainer = findViewById(R.id.reportsContainer);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the currently logged-in user's ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser != null ? currentUser.getUid() : null;

        // Fetch reports from Firestore
        fetchReports();

        // Network state
        if (!NetworkUtil.isConnected(this)) {
            showOfflineError();
        }
    }

    private void fetchReports() {
        if (currentUserId == null) {
            Log.e(TAG, "No logged-in user.");
            return;
        }

        CollectionReference reportsRef = db.collection("reports");

        reportsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // No data fetched, display a message
                    TextView noDataTextView = new TextView(this);
                    noDataTextView.setText("No data fetched");
                    reportsContainer.addView(noDataTextView);
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "Report ID: " + document.getId());
                        Map<String, Object> report = document.getData();
                        Log.d(TAG, "Adding report: " + report.get("reportTitle"));
                        addReportToView(document.getId(), report);
                    }
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    private void addReportToView(String reportId, Map<String, Object> report) {
        // Create a container for each report
        LinearLayout reportContainer = new LinearLayout(this);
        reportContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        reportContainer.setOrientation(LinearLayout.VERTICAL);
        reportContainer.setPadding(16, 16, 16, 16);

        // Create a TextView for the report info
        TextView reportView = new TextView(this);
        reportView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        reportView.setPadding(16, 16, 16, 16);

        String reportInfo = (String) report.get("reportTitle");
        reportView.setText(reportInfo);

        // Set an OnClickListener to open the ChatActivity for the selected report
        reportContainer.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
            intent.putExtra("reportId", reportId);
            startActivity(intent);
        });

        // Add the TextView to the report container
        reportContainer.addView(reportView);

        // Add the report container to the main reports container
        reportsContainer.addView(reportContainer);

        // Add a divider view
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1 // 1dp height for the divider
        ));
        divider.setBackgroundResource(R.drawable.divider);
        reportsContainer.addView(divider);
    }

    private void showOfflineError() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

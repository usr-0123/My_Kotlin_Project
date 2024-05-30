package com.example.myapplication.home.ui.reports

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class SlideshowViewModel : ViewModel() {

    private val _reports = MutableLiveData<List<DataSnapshot>>()
    val reports: LiveData<List<DataSnapshot>> = _reports

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("reports")

    init {
        fetchReports()
    }

    private fun fetchReports() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reportList = mutableListOf<DataSnapshot>()
                for (reportSnapshot in snapshot.children) {
                    Log.d("SlideshowViewModel", "Report ID: ${reportSnapshot.key}")
                    reportList.add(reportSnapshot)
                }
                _reports.value = reportList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("SlideshowViewModel", "Error fetching reports", error.toException())
            }
        })
    }
}

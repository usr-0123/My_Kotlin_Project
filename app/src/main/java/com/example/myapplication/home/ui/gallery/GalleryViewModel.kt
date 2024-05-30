// GalleryViewModel.kt
package com.example.myapplication.home.ui.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class GalleryViewModel : ViewModel() {

    private val _reports = MutableLiveData<List<QueryDocumentSnapshot>>()
    val reports: LiveData<List<QueryDocumentSnapshot>> = _reports

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchReports()
    }

    private fun fetchReports() {
        val reportsRef = db.collection("reports")
        reportsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reportList = mutableListOf<QueryDocumentSnapshot>()
                for (document in task.result!!) {
                    Log.d("GalleryViewModel", "Report ID: ${document.id}")
                    reportList.add(document)
                }
                _reports.value = reportList
            } else {
                Log.w("GalleryViewModel", "Error getting documents.", task.exception)
            }
        }
    }
}

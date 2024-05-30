// GalleryViewModel.kt
package com.example.myapplication.home.ui.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class GalleryViewModel : ViewModel() {

    private val _groups = MutableLiveData<List<DataSnapshot>>()
    val groups: LiveData<List<DataSnapshot>> = _groups

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("groups")

    init {
        fetchGroups()
    }

    private fun fetchGroups() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val  groupList = mutableListOf<DataSnapshot>()
                for (groupSnapshot in snapshot.children) {
                    Log.d("GalleryViewModel","Group ID: ${groupSnapshot.key}")
                    groupList.add(groupSnapshot)
                }
                _groups.value = groupList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("GalleryViewModel","Error fetching groups",error.toException())
            }
        })
    }
}

package com.example.myapplication.home.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.home.ui.threads.model.Post
import com.google.firebase.database.*

class HomeViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("threads")

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = mutableListOf<Post>()
                for (threadSnapshot in snapshot.children) {
                    val post = threadSnapshot.getValue(Post::class.java)
                    post?.let {
                        postList.add(it)
                    }
                }
                _posts.value = postList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}

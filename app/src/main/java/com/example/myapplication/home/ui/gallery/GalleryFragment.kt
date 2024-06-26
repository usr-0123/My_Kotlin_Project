// GalleryFragment.kt
package com.example.myapplication.home.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentGalleryBinding
import com.example.myapplication.home.ui.gallery.activity.GroupActivity
import com.google.firebase.database.DataSnapshot

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val groupsContainer: LinearLayout = binding.reportsContainer

        galleryViewModel.groups.observe(viewLifecycleOwner) { groups ->
            groupsContainer.removeAllViews()
            for (group in groups) {
                addReportToView(group, groupsContainer)
            }
        }

        return root
    }

    private fun addReportToView(group: DataSnapshot, container: LinearLayout) {
        val groupContainer = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val groupView = TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
            text = group.child("groupName").getValue(String::class.java)
        }

        groupContainer.addView(groupView)

        // Add click listener to open ChatActivity
        groupContainer.setOnClickListener {
            val intent = Intent(requireContext(), GroupActivity::class.java)
            intent.putExtra("groupId", group.key)
            intent.putExtra("groupTitle",group.child("groupName").getValue(String::class.java))
            startActivity(intent)
        }

        container.addView(groupContainer)

        val divider = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1 // 1dp height for the divider
            )
            setBackgroundResource(R.drawable.divider)
        }
        container.addView(divider)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

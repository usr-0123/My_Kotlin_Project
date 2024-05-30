package com.example.myapplication.home.ui.reports

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
import com.example.myapplication.databinding.FragmentSlideshowBinding
import com.example.myapplication.home.ui.reports.activity.ReportActivity
import com.google.firebase.database.DataSnapshot

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val reportsContainer: LinearLayout = binding.reportsContainer

        slideshowViewModel.reports.observe(viewLifecycleOwner) { reports ->
            reportsContainer.removeAllViews()
            for (report in reports) {
                addReportToView(report, reportsContainer)
            }
        }
        return root
    }

    private fun addReportToView(report: DataSnapshot, container: LinearLayout) {
        val reportContainer = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val reportView = TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
            text = report.child("reportTitle").getValue(String::class.java)
        }

        reportContainer.addView(reportView)

        val reportMessageView = TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
            text = report.child("message").getValue(String::class.java)
        }

        reportContainer.addView(reportMessageView)

        // Add click listener to open ChatActivity
        reportContainer.setOnClickListener {
            val intent = Intent(requireContext(), ReportActivity::class.java)
            intent.putExtra("reportId", report.key)
            intent.putExtra("reportName", report.child("reportTitle").getValue(String::class.java))
            startActivity(intent)
        }

        container.addView(reportContainer)

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

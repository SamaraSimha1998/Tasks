package com.example.tasks.aapoonLoginPage.dashboard.circles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tasks.databinding.FragmentCirclesBinding

class CirclesFragment: Fragment() {

    private var _binding: FragmentCirclesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCirclesBinding.inflate(inflater, container, false)
        // connected circles are shown from here.
        binding.btnAddCircle.setOnClickListener {
            Toast.makeText(context, "add button clicked", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
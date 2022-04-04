package com.example.tasks.aapoonLoginPage.dashboard.circles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tasks.R

class CirclesFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_circles, container, false)
        // connected circles are shown from here.
        view.findViewById<Button>(R.id.btn_add_circle).setOnClickListener {
            Toast.makeText(context, "add button clicked", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}
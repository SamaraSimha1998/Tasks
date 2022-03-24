package com.example.tasks.aapoonLoginPage.dashboard.circles

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tasks.R
import kotlinx.android.synthetic.main.fragment_circles.view.*

class CirclesFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_circles, container, false)
        // connected circles are shown from here.
        view.btn_add_circle.setOnClickListener {
            Toast.makeText(context, "add button clicked", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}
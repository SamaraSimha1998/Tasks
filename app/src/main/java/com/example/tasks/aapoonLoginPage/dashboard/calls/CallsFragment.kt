package com.example.tasks.aapoonLoginPage.dashboard.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tasks.R

class CallsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // calls are shown here
        return inflater.inflate(R.layout.fragment_calls, container, false)
    }
}
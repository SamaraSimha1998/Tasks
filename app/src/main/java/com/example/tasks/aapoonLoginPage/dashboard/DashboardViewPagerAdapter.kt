package com.example.tasks.aapoonLoginPage.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tasks.aapoonLoginPage.dashboard.calls.CallsFragment
import com.example.tasks.aapoonLoginPage.dashboard.chat.ChatFragment
import com.example.tasks.aapoonLoginPage.dashboard.circles.CirclesFragment
import com.example.tasks.aapoonLoginPage.dashboard.connect.ConnectFragment
import com.google.firebase.auth.FirebaseAuth

class DashboardViewPagerAdapter(fragmentManager: FragmentManager, fragmentLifecycle: Lifecycle, val phoneNumber: String) :
    FragmentStateAdapter(fragmentManager, fragmentLifecycle) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getItemCount(): Int {
        return 4
    }

    // Adapter for Tabs
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ChatFragment(phoneNumber)
            }
            1 -> {
                CirclesFragment()
            }
            2 -> {
                ConnectFragment(auth.currentUser!!.uid)
            }
            3 -> {
                CallsFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}
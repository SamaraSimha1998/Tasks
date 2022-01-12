package com.example.tasks.tabProfile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, fragmentLifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, fragmentLifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ProfileSaveFragment()
            }
            1 -> {
                ProfileDisplayCardFragment()
            }
            2 -> {
                ContactsFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}
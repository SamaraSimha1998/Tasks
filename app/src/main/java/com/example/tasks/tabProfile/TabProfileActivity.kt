package com.example.tasks.tabProfile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.databinding.ActivityTabProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTabProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileViewPager.adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)

        // Makes title and position to every tab as below
        TabLayoutMediator(binding.profileTabLayout, binding.profileViewPager){ tab,position ->
            when (position) {
                0 -> {
                     tab.text = "create data"
                }
                1 -> {
                    tab.text = "display data"
                }
                2 -> {
                    tab.text = "contacts"
                }
            }
        }.attach()
    }
}
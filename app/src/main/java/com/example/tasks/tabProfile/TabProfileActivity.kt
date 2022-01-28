package com.example.tasks.tabProfile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_tab_profile.*

class TabProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_profile)

        profile_view_pager.adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)

        // Makes title and position to every tab as below
        TabLayoutMediator(profile_tab_layout, profile_view_pager){ tab,position ->
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
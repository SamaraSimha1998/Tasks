package com.example.tasks.aapoonLoginPage.dashboard.connect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity

class GroupChannelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_channel)
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashBoardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
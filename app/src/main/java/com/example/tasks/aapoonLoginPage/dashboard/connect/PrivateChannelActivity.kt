package com.example.tasks.aapoonLoginPage.dashboard.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasks.R
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity

class PrivateChannelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_channel)
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashBoardActivity::class.java)
        startActivity(intent)
    }
}
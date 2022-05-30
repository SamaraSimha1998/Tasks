package com.example.tasks.aapoonLoginPage.menuView

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity
import com.example.tasks.databinding.ActivitySupportTeamBinding

class SupportTeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportTeamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitIssue.setOnClickListener {
            val subject = binding.topicSelectEditText.text.toString()
            val body = binding.descriptionEditText.text.toString()
            MailSender().sendMail(this@SupportTeamActivity, subject, body)

            Toast.makeText(this@SupportTeamActivity, "Issue submitted successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashBoardActivity::class.java)
        startActivity(intent)
    }
}
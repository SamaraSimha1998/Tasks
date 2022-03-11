package com.example.tasks.aapoonLoginPage.menuView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_referral.*

class ReferralActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referral)

        refer_a_friend_text_view.setOnClickListener {
            Toast.makeText(this, "Code Referred!!", Toast.LENGTH_SHORT).show()
        }
    }
}
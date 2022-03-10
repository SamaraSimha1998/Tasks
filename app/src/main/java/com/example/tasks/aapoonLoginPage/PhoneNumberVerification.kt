package com.example.tasks.aapoonLoginPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.MainActivity
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_phone_number_verification.*

class PhoneNumberVerification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number_verification)

        country_code_picker.registerCarrierNumberEditText(phone_number_edit_text)

        btn_get_otp.setOnClickListener {
            val intent = Intent(this,ManageOtp::class.java)
            intent.putExtra("phoneNumber",country_code_picker.fullNumberWithPlus.replace(" ",""))
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
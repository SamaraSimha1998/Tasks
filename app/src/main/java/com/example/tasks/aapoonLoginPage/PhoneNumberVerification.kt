package com.example.tasks.aapoonLoginPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.MainActivity
import com.example.tasks.databinding.ActivityPhoneNumberVerificationBinding

class PhoneNumberVerification : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneNumberVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.countryCodePicker.registerCarrierNumberEditText(binding.phoneNumberEditText)

        binding.btnGetOtp.setOnClickListener {
            val intent = Intent(this,ManageOtp::class.java)
            intent.putExtra("phoneNumber",binding.countryCodePicker.fullNumberWithPlus.replace(" ",""))
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
package com.example.tasks.aapoonLoginPage.menuView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasks.databinding.ActivityUpdateNumberBinding

class UpdateNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.verifyCountryCodePicker.registerCarrierNumberEditText(binding.verifyPhoneNumberEditText)

        binding.btnVerifyPhoneNumber.setOnClickListener {
            val intent = Intent(this, VerifyNumberActivity::class.java)
            intent.putExtra("updatePhoneNumber",binding.verifyCountryCodePicker.fullNumberWithPlus.replace(" ",""))
            startActivity(intent)
        }
    }
}
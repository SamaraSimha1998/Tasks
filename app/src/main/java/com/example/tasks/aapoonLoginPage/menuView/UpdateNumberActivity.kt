package com.example.tasks.aapoonLoginPage.menuView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_update_number.*

class UpdateNumberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_number)

        verify_country_code_picker.registerCarrierNumberEditText(verify_phone_number_edit_text)

        btn_verify_phone_number.setOnClickListener {
            val intent = Intent(this, VerifyNumberActivity::class.java)
            intent.putExtra("updatePhoneNumber",verify_country_code_picker.fullNumberWithPlus.replace(" ",""))
            startActivity(intent)
        }
    }
}
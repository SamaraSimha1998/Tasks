package com.example.tasks.aapoonLoginPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_user_categories.*

class UserCategories : AppCompatActivity() {

    private var radioButton: String? = null
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_categories)

        phoneNumber = intent.getStringExtra("phoneNumber").toString()

        btn_continue.setOnClickListener {
            when (radioButton) {
                "btn_business_user_radio" -> {
                    val intent = Intent(this,FillProfileActivity::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                }
                "btn_individual_user_radio" -> {
                    val intent = Intent(this,FillProfileActivity::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                }
                null -> {
                    Toast.makeText(this,"Please select an option",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.btn_business_user_radio ->
                    if (checked) {
                        radioButton = "btn_business_user_radio"
                    }
                R.id.btn_individual_user_radio ->
                    if (checked) {
                        radioButton = "btn_individual_user_radio"
                    }
            }
        }
    }

    fun onBack() {

    }
}
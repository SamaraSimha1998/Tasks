package com.example.tasks.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile_view.*


class ProfileViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.tasks.R.layout.activity_profile_view)

        val bundle = intent.extras
        first_name_text_view.text = bundle!!.getString("firstName")
        last_name_text_view.text = bundle.getString("lastName")
        gender_text_view.text = bundle.getString("gender")
        dob_text_view.text = bundle.getString("dob")
        email_text_view.text = bundle.getString("email")
        phone_text_view.text = bundle.getString("phone")

        Toast.makeText(this, "Fetched Successfully!", Toast.LENGTH_SHORT).show()
    }
}
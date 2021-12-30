package com.example.tasks.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasks.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile_log.*
import kotlinx.android.synthetic.main.activity_profile_view.*

open class ProfileLogActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_log)

        btn_add_profile.setOnClickListener {
            val intent = Intent(this@ProfileLogActivity, ProfileSaveActivity::class.java)
            startActivity(intent)
        }

        btn_get_profile.setOnClickListener {
            val emailId : String = email_get_profile.text.toString()
            val userEmail = emailId.replace(".",",")
            if (emailId.isNotEmpty()) {
                readData(userEmail)
                email_get_profile.text.clear()
            } else {
                Toast.makeText(this,"Please enter valid email!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun readData(emailId: String) {
        database = FirebaseDatabase.getInstance().getReference("Profiles")
        database.child(emailId).get().addOnSuccessListener {
            when {
                it.exists() -> {

                    val firstName = it.child("firstName").value.toString()
                    val lastName = it.child("lastName").value.toString()
                    val gender = it.child("gender").value.toString()
                    val dob = it.child("dob").value.toString()
                    val phone = it.child("phone").value.toString()
                    val email = it.child("email").value.toString()
                    val image = it.child("image").value.toString()

                    val intent = Intent(this@ProfileLogActivity, ProfileViewActivity::class.java)
                    intent.putExtra("firstName",firstName)
                    intent.putExtra("lastName",lastName)
                    intent.putExtra("gender",gender)
                    intent.putExtra("dob",dob)
                    intent.putExtra("phone",phone)
                    intent.putExtra("email",email)
                    intent.putExtra("image",image)
                    startActivity(intent)

                }
                else -> {
                    Toast.makeText(this,"Profile doesn't exist!!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
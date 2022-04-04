package com.example.tasks.aapoonLoginPage.menuView

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity
import com.example.tasks.databinding.ActivityAppProfileViewBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AppProfileViewActivity : AppCompatActivity() {

    private lateinit var phoneNumber : String
    private lateinit var database : DatabaseReference
    private lateinit var baseImage : String
    private lateinit var binding: ActivityAppProfileViewBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.getStringExtra("phoneNumber").toString()

        initComponent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComponent() {

        database = FirebaseDatabase.getInstance().getReference("AppProfiles")
        database.child(phoneNumber).get().addOnSuccessListener {
            when {
                it.exists() -> {
                    baseImage = it.child("image").value.toString()
                    val firstName = it.child("firstName").value.toString()
                    val lastName = it.child("lastName").value.toString()
                    val gender = it.child("gender").value.toString()
                    val dob = it.child("dob").value.toString()
                    val email = it.child("email").value.toString()
                    val phone = it.child("phone").value.toString()

                    binding.appFirstNameTextView.text = firstName
                    binding.appLastNameTextView.text = lastName
                    binding.appGenderTextView.text = gender
                    binding.appDobTextView.text = dob
                    binding.appEmailTextView.text = email
                    binding.appPhoneTextView.text = phone
                    binding.appUserImageView.setImageBitmap(base64ToBitmap(baseImage))
                }
            }
        }
    }

    // converts bitmap image to normal image
    @RequiresApi(Build.VERSION_CODES.O)
    private fun base64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes: ByteArray = Base64.getDecoder().decode(b64)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashBoardActivity::class.java)
        intent.putExtra("phoneNumber",phoneNumber)
        startActivity(intent)
        finish()
    }
}
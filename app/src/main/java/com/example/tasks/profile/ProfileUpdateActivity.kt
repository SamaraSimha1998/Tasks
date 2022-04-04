package com.example.tasks.profile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.databinding.ActivityProfileUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileUpdateActivity : AppCompatActivity() {
    private lateinit var baseImage: String
    private lateinit var database: DatabaseReference
    private lateinit var emailId : String
    private lateinit var pastImage: String
    private lateinit var binding: ActivityProfileUpdateBinding

    @SuppressLint("SetTextI18n", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        emailId = bundle!!.getString("emailId").toString()
        pastImage = bundle.getString("baseImage").toString()

        binding.calenderUpdateImageView.setOnClickListener { selectDate() }

        binding.dobUpdateEditText.setOnClickListener { selectDate() }

        binding.profileUpdateImageView.setOnClickListener { takePictureIntent() }

        binding.btnUpdateData.setOnClickListener { updateProfile() }
    }

    private val datePickerId = 2
    private val requestImageCapture = 1

    private fun selectDate() {
        showDialog(datePickerId)
    }

    // Creates calender dialog box
    override fun onCreateDialog(id: Int): Dialog? {
        val c: Calendar = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        when (id) {
            datePickerId ->
                return DatePickerDialog(this, pickerListener, year, month, day)
        }
        return null
    }

    // Selects date from calender
    private val pickerListener =
        DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            binding.dobUpdateEditText.setText(
                StringBuilder().append(selectedDay)
                    .append("/").append(selectedMonth + 1).append("/").append(selectedYear)
                    .append(" ")
            )
        }

    // Opens camera to take picture
    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, requestImageCapture)
        } catch (e: ActivityNotFoundException) {
        }
    }

    // Processes taken picture into bitmap
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        baseImage = if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            binding.profileUpdateImageView.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }else {
            pastImage
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateProfile(){

        val firstName =  binding.firstNameUpdateEditText.text.toString()
        val lastName = binding.lastNameUpdateEditText.text.toString()

        val gender : String = if (binding.radioGroupUpdate.checkedRadioButtonId == binding.radioMale.id) {
            "Male"
        }else{
            "Female"
        }
        val dob = binding.dobUpdateEditText.text.toString()
        val phone = binding.phoneUpdateEditText.text.toString()
        val email = emailId
        val image = baseImage

        database = FirebaseDatabase.getInstance().getReference("Profiles")

        val userEmail = email.replace(".",",")
        val profile = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "gender" to gender,
            "dob" to dob,
            "phone" to phone,
            "email" to email,
            "image" to image)

        // Updates data by verifying email from database
        try {
            database.child(userEmail).updateChildren(profile)
            val intent = Intent(this, ProfileLogActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,"Updated Successfully!", Toast.LENGTH_SHORT).show()
        }catch (e:Exception) {
            val intent = Intent(this, ProfileViewActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,"Failed to update!!", Toast.LENGTH_SHORT).show()
        }
    }
}
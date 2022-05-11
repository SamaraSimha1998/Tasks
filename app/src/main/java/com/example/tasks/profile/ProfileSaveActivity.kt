package com.example.tasks.profile

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.*
import android.graphics.BitmapFactory
import com.example.tasks.R
import com.example.tasks.databinding.ActivityProfileSaveBinding


@Suppress("DEPRECATION")
class ProfileSaveActivity : AppCompatActivity() {

    private lateinit var baseImage: String
    private lateinit var database: DatabaseReference
    private var emailId: String = ""
    private lateinit var binding: ActivityProfileSaveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calenderImageView.setOnClickListener { selectDate() }

        binding.dobEditText.setOnClickListener { selectDate() }

        binding.profileImageView.setOnClickListener { takePictureIntent() }

        binding.btnSaveProfile.setOnClickListener { checkRequirements() }
    }

    private val datePickerId = 2
    private val requestImageCapture = 1

    private fun selectDate() {
        showDialog(datePickerId)
    }

    // Creates calender dialog box
    @Deprecated("Deprecated in Java")
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

    // Picks date from calender
    private val pickerListener = OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            binding.dobEditText.setText(
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

    // Processes the taken picture into bitmap
    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        baseImage = if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            binding.profileImageView.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }else {
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.aapoon_logo)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            binding.profileImageView.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }
    }

    private fun addProfile(){
        val firstName =  binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()

        val gender : String = if (binding.radioGroupSave.checkedRadioButtonId == binding.radioMaleSave.id) {
            "Male"
        }else{
            "Female"
        }
        val dob = binding.dobEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val image = baseImage

        database = FirebaseDatabase.getInstance().getReference("Profiles")

        val profile = Profile(firstName, lastName, gender, dob, phone, email, image)
        val userEmail = email.replace(".",",")

        try {
            database.child(userEmail).setValue(profile)
            val intent = Intent(this, ProfileLogActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,"Saved Successfully!",Toast.LENGTH_SHORT).show()
        }catch (e:Exception) {
            Toast.makeText(this,"Failed to save!!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ProfileLogActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkRequirements(){
        emailId = binding.emailEditText.text.toString()
        if( emailId == ""){
            Toast.makeText(this,"please enter email id",Toast.LENGTH_SHORT).show()
        }else {
            addProfile()
        }
    }
}
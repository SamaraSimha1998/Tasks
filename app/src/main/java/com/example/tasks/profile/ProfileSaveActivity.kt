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
import kotlinx.android.synthetic.main.activity_profile_save.*
import java.io.ByteArrayOutputStream
import java.util.*
import android.graphics.BitmapFactory
import com.example.tasks.R


class ProfileSaveActivity : AppCompatActivity() {

    private lateinit var baseImage: String
    private lateinit var database: DatabaseReference
    private var emailId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_save)

        calender_image_view.setOnClickListener { selectDate() }

        dob_edit_text.setOnClickListener { selectDate() }

        profile_image_view.setOnClickListener { takePictureIntent() }

        btn_save_profile.setOnClickListener { checkRequirements() }
    }

    private val datePickerId = 2
    private val requestImageCapture = 1

    private fun selectDate() {
        showDialog(datePickerId)
    }

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

    private val pickerListener = OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            dob_edit_text.setText(
                StringBuilder().append(selectedDay)
                    .append("/").append(selectedMonth + 1).append("/").append(selectedYear)
                    .append(" ")
            )
    }

    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, requestImageCapture)
        } catch (e: ActivityNotFoundException) {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        baseImage = if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            profile_image_view.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }else {
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.aapoon_logo)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            profile_image_view.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }
    }

    private fun addProfile(){
        val firstName =  first_name_edit_text.text.toString()
        val lastName = last_name_edit_text.text.toString()

        val gender : String = if (radio_group.checkedRadioButtonId == 2131231307) {
            "Male"
        }else{
            "Female"
        }
        val dob = dob_edit_text.text.toString()
        val phone = phone_edit_text.text.toString()
        val email = email_edit_text.text.toString()
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
        emailId = email_edit_text.text.toString()
        if( emailId == ""){
            Toast.makeText(this,"please enter email id",Toast.LENGTH_SHORT).show()
        }else {
            addProfile()
        }
    }
}
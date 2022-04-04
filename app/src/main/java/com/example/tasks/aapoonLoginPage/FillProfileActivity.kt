package com.example.tasks.aapoonLoginPage

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity
import com.example.tasks.databinding.ActivityFillProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.*

class FillProfileActivity : AppCompatActivity() {

    private lateinit var baseImage: String
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var phoneNumber: String
    private var emailId: String = ""
    private var logProgress: Array<String> = arrayOf("Details","ProfilePic","Completed")
    private var currentState = 0
    private lateinit var binding: ActivityFillProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        phoneNumber = intent.getStringExtra("phoneNumber").toString()

        binding.stepsView.setLabels(logProgress)
            .setBarColorIndicator(Color.WHITE)
            .setProgressColorIndicator(R.color.start_received)
            .setLabelColorIndicator(R.color.start_received)
            .setCompletedPosition(currentState)
            .drawView()

        binding.stepsView.completedPosition = currentState

        binding.appCalenderImageView.setOnClickListener { selectDate() }

        binding.appDobEditText.setOnClickListener { selectDate() }

        binding.btnAppSaveProfile.setOnClickListener { checkRequirements() }

        binding.appProfileImageView.setOnClickListener { takePictureIntent() }

        binding.btnAppSaveProfilePic.setOnClickListener { saveImage() }

        binding.btnCompletedProifleDetails.setOnClickListener {

            val intent = Intent(this, DashBoardActivity::class.java)
            intent.putExtra("phoneNumber",phoneNumber)
            startActivity(intent)

            Toast.makeText(this,"Successfully Completed", Toast.LENGTH_SHORT).show()
        }
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

    // Picks date from calender
    private val pickerListener =
        DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            binding.appDobEditText.setText(
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        baseImage = if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            binding.appProfileImageView.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }else {
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.aapoon_logo)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            binding.appProfileImageView.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }
    }

    private fun addProfile(){
        val firstName =  binding.appFirstNameEditText.text.toString()
        val lastName = binding.appLastNameEditText.text.toString()
        val gender : String = if (binding.appRadioGroup.checkedRadioButtonId == binding.appRadioMale.id) {
            "Male"
        }else{
            "Female"
        }
        val dob = binding.appDobEditText.text.toString()
        val phone = phoneNumber
        val email = binding.appEmailEditText.text.toString()
        val uid = auth.currentUser!!.uid

        database = FirebaseDatabase.getInstance().getReference("AppProfiles")

        val profile = AppProfile(firstName, lastName, gender, dob, phone, email, null, uid)

        try {
            database.child(phone).setValue(profile)
            progress()
            visibilityImage()
            Toast.makeText(this,"Saved Successfully!", Toast.LENGTH_SHORT).show()
        }catch (e:Exception) {
            Toast.makeText(this,"Failed to save!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkRequirements(){
        emailId = binding.appEmailEditText.text.toString()
        if( emailId == ""){
            Toast.makeText(this,"please enter email id", Toast.LENGTH_SHORT).show()
        }else {
            addProfile()
        }
    }

    private fun progress() {
        if(currentState < logProgress.size - 1) {
            currentState += 1
            binding.stepsView.setCompletedPosition(currentState).drawView()
        }
    }

    private fun visibilityImage() {
        binding.appFirstNameEditText.visibility = View.INVISIBLE
        binding.appLastNameEditText.visibility = View.INVISIBLE
        binding.appTextView.visibility = View.INVISIBLE
        binding.appRadioGroup.visibility = View.INVISIBLE
        binding.appLinearLayout5.visibility = View.INVISIBLE
        binding.appLinearLayout4.visibility = View.INVISIBLE
//        app_linearLayout6.visibility = View.INVISIBLE
        binding.btnAppSaveProfile.visibility = View.INVISIBLE
        binding.appProfileImageView.visibility = View.VISIBLE
        binding.btnAppSaveProfilePic.visibility = View.VISIBLE
    }

    private fun visibilityCompleted() {
        binding.appProfileImageView.visibility = View.INVISIBLE
        binding.btnAppSaveProfilePic.visibility = View.INVISIBLE
        binding.completedImageView.visibility = View.VISIBLE
        binding.completedTextView.visibility = View.VISIBLE
        binding.btnCompletedProifleDetails.visibility = View.VISIBLE
    }

    private fun saveImage() {
        val image = baseImage
        val firstName =  binding.appFirstNameEditText.text.toString()
        val lastName = binding.appLastNameEditText.text.toString()

        val gender : String = if (binding.appRadioGroup.checkedRadioButtonId == binding.appRadioMale.id) {
            "Male"
        }else{
            "Female"
        }
        val dob = binding.appDobEditText.text.toString()
        val phone = phoneNumber
        val email = binding.appEmailEditText.text.toString()
        val uid = auth.currentUser!!.uid

        database = FirebaseDatabase.getInstance().getReference("AppProfiles")

        val profile = AppProfile(firstName, lastName, gender, dob, phone, email, image, uid)
        database.child(phone).setValue(profile)

        try {
            database.child(phone).setValue(profile)
            progress()
            visibilityCompleted()
            Toast.makeText(this,"Saved Successfully!", Toast.LENGTH_SHORT).show()
        }catch (e:Exception) {
            Toast.makeText(this,"Failed to save!!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
        Toast.makeText(this,"Signin Failed!!",Toast.LENGTH_SHORT).show()
        val intent = Intent(this, PhoneNumberVerification::class.java)
        startActivity(intent)
        finish()
    }
}
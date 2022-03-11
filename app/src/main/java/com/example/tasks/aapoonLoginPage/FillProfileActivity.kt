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
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.example.tasks.aapoonLoginPage.menuView.DashBoardActivity
import com.example.tasks.profile.Profile
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_fill_profile.*
import java.io.ByteArrayOutputStream
import java.util.*

class FillProfileActivity : AppCompatActivity() {

    private lateinit var baseImage: String
    private lateinit var database: DatabaseReference
    private lateinit var phoneNumber: String
    private var emailId: String = ""
    private var logProgress: Array<String> = arrayOf("Details","ProfilePic","Completed")
    private var currentState = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_profile)

        phoneNumber = intent.getStringExtra("phoneNumber").toString()

        steps_view.setLabels(logProgress)
            .setBarColorIndicator(Color.WHITE)
            .setProgressColorIndicator(R.color.start_received)
            .setLabelColorIndicator(R.color.start_received)
            .setCompletedPosition(currentState)
            .drawView()

        steps_view.completedPosition = currentState

        app_calender_image_view.setOnClickListener { selectDate() }

        app_dob_edit_text.setOnClickListener { selectDate() }

        btn_app_save_profile.setOnClickListener { checkRequirements() }

        app_profile_image_view.setOnClickListener { takePictureIntent() }

        btn_app_save_profile_pic.setOnClickListener { saveImage() }

        btn_completed_proifle_details.setOnClickListener {
//            val intent = Intent(this, AppProfileViewActivity::class.java)
//            startActivity(intent)

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
            app_dob_edit_text.setText(
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
            app_profile_image_view.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }else {
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.aapoon_logo)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            app_profile_image_view.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }
    }

    private fun addProfile(){
        val firstName =  app_first_name_edit_text.text.toString()
        val lastName = app_last_name_edit_text.text.toString()
        val gender : String = if (app_radio_group.checkedRadioButtonId == app_radio_male.id) {
            "Male"
        }else{
            "Female"
        }
        val dob = app_dob_edit_text.text.toString()
        val phone = phoneNumber
        val email = app_email_edit_text.text.toString()

        database = FirebaseDatabase.getInstance().getReference("AppProfiles")

        val profile = Profile(firstName, lastName, gender, dob, phone, email, null)

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
        emailId = app_email_edit_text.text.toString()
        if( emailId == ""){
            Toast.makeText(this,"please enter email id", Toast.LENGTH_SHORT).show()
        }else {
            addProfile()
        }
    }

    private fun progress() {
        if(currentState < logProgress.size - 1) {
            currentState += 1
            steps_view.setCompletedPosition(currentState).drawView()
        }
    }

    private fun visibilityImage() {
        app_first_name_edit_text.visibility = View.INVISIBLE
        app_last_name_edit_text.visibility = View.INVISIBLE
        app_textView.visibility = View.INVISIBLE
        app_radio_group.visibility = View.INVISIBLE
        app_linearLayout5.visibility = View.INVISIBLE
        app_linearLayout4.visibility = View.INVISIBLE
        app_linearLayout6.visibility = View.INVISIBLE
        btn_app_save_profile.visibility = View.INVISIBLE
        app_profile_image_view.visibility = View.VISIBLE
        btn_app_save_profile_pic.visibility = View.VISIBLE
    }

    private fun visibilityCompleted() {
        app_profile_image_view.visibility = View.INVISIBLE
        btn_app_save_profile_pic.visibility = View.INVISIBLE
        completed_image_view.visibility = View.VISIBLE
        completed_text_view.visibility = View.VISIBLE
        btn_completed_proifle_details.visibility = View.VISIBLE
    }

    private fun saveImage() {
        val image = baseImage

        val firstName =  app_first_name_edit_text.text.toString()
        val lastName = app_last_name_edit_text.text.toString()

        val gender : String = if (app_radio_group.checkedRadioButtonId == app_radio_male.id) {
            Log.d("radioMale",app_radio_male.id.toString())
            "Male"
        }else{
            "Female"
        }
        val dob = app_dob_edit_text.text.toString()
        val phone = phoneNumber
        val email = app_email_edit_text.text.toString()

        database = FirebaseDatabase.getInstance().getReference("AppProfiles")

        val profile = Profile(firstName, lastName, gender, dob, phone, email, image)

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
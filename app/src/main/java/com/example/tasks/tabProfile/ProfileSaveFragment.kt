package com.example.tasks.tabProfile

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tasks.R
import com.example.tasks.profile.Profile
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_profile_save.*
import kotlinx.android.synthetic.main.fragment_profile_save.view.*
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileSaveFragment : Fragment() {

    private lateinit var baseImage: String
    private lateinit var database: DatabaseReference
    private var emailId: String = ""
    private val sharedPrefFile = "profileSharedPreference"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val view: View = inflater.inflate(R.layout.fragment_profile_save, container, false)

        view.tab_calender_image_view.setOnClickListener { selectDate() }

        view.tab_dob_edit_text.setOnClickListener { selectDate() }

        view.tab_profile_image_view.setOnClickListener { takePictureIntent() }

        view.btn_tab_save_profile.setOnClickListener { checkRequirements() }

        return view
    }

    private fun checkRequirements(){
        emailId = tab_email_edit_text.text.toString()
        if( emailId == ""){
            Toast.makeText(activity,"please enter email id", Toast.LENGTH_SHORT).show()
        }else {
            addProfile()
        }
    }

    private fun addProfile(){

        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        val firstName =  tab_first_name_edit_text.text.toString()
        val lastName = tab_last_name_edit_text.text.toString()

        val gender : String = if (tab_radio_group.checkedRadioButtonId == 2131231307) {
            "Male"
        }else{
            "Female"
        }
        val dob = tab_dob_edit_text.text.toString()
        val phone = tab_phone_edit_text.text.toString()
        val email = tab_email_edit_text.text.toString()
        val image = baseImage

        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString("firstName",firstName)
        editor?.putString("lastName",lastName)
        editor?.putString("gender",gender)
        editor?.putString("dob",dob)
        editor?.putString("phone",phone)
        editor?.putString("email",email)
        editor?.putString("image",image)
        editor?.apply()

        database = FirebaseDatabase.getInstance().getReference("Model")

        val profile = Profile(firstName, lastName, gender, dob, phone, email, image)
        val userEmail = email.replace(".",",")

        try {
            database.child(userEmail).setValue(profile)
            Toast.makeText(activity,"Saved Successfully!",Toast.LENGTH_SHORT).show()
            tab_profile_image_view.setImageDrawable(null)
            tab_email_edit_text.text = null
            tab_first_name_edit_text.text = null
            tab_last_name_edit_text.text = null
            tab_phone_edit_text.text = null
        }catch (e:Exception) {
            Toast.makeText(activity,"Failed to save!!",Toast.LENGTH_SHORT).show()
        }
    }

    private val datePickerId = 2
    private val requestImageCapture = 1

    private fun selectDate() {
        datePickerId.onCreateDialog()?.show()
    }

    private fun Int.onCreateDialog(): Dialog? {
        val c: Calendar = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        when (this) {
            datePickerId ->
                return DatePickerDialog(requireActivity(), pickerListener, year, month, day)
        }
        return null
    }

    private val pickerListener =
        DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            tab_dob_edit_text.setText(
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
        baseImage = if (requestCode == requestImageCapture && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            tab_profile_image_view.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }else {
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.aapoon_logo)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            tab_profile_image_view.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }
    }
}
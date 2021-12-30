package com.example.tasks.profile

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile_view.*
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap





class ProfileViewActivity : AppCompatActivity() {

    private lateinit var baseImage : String

    @RequiresApi(Build.VERSION_CODES.O)
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
        baseImage = bundle.getString("image").toString()

        user_image_view.setImageBitmap(base64ToBitmap(baseImage))

        Toast.makeText(this, "Fetched Successfully!", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun base64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes: ByteArray = Base64.getDecoder().decode(b64)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }
}
package com.example.tasks.aapoonLoginPage

import com.example.tasks.profile.ProfileLogActivity
import com.example.tasks.profile.ProfileUpdateActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_app_profile_view.*
import java.util.*

class AppProfileViewActivity : AppCompatActivity() {

    private lateinit var baseImage : String
    private lateinit var toggle: ActionBarDrawerToggle

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_profile_view)

        toggle = ActionBarDrawerToggle(this, app_profile_menu_drawer_layout, R.string.open, R.string.close)
        app_profile_menu_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        app_profile_menu_nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btn_edit_menu -> edit()
                R.id.btn_exit_profile -> exitProfile()
            }
            true
        }

        val bundle = intent.extras
        app_first_name_text_view.text = bundle!!.getString("firstName")
        app_last_name_text_view.text = bundle.getString("lastName")
        app_gender_text_view.text = bundle.getString("gender")
        app_dob_text_view.text = bundle.getString("dob")
        app_email_text_view.text = bundle.getString("email")
        app_phone_text_view.text = bundle.getString("phone")
        baseImage = bundle.getString("image").toString()

        app_user_image_view.setImageBitmap(base64ToBitmap(baseImage))

        Toast.makeText(this, "Fetched Successfully!", Toast.LENGTH_SHORT).show()
    }
    // For bottom menu navigation
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // converts bitmap image to normal image
    @RequiresApi(Build.VERSION_CODES.O)
    private fun base64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes: ByteArray = Base64.getDecoder().decode(b64)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }

    private fun edit() {
        val intent = Intent(this, ProfileUpdateActivity::class.java)
        startActivity(intent)
        val emailId = app_email_text_view.text.toString()
        val intentEmail = Intent(this@AppProfileViewActivity, ProfileUpdateActivity::class.java)
        intentEmail.putExtra("emailId",emailId)
        intentEmail.putExtra("baseImage",baseImage)
        startActivity(intentEmail)

        Toast.makeText(this, "Edit button clicked", Toast.LENGTH_SHORT).show()
    }

    private fun exitProfile() {
        val intent = Intent(this@AppProfileViewActivity, ProfileLogActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Exit button clicked", Toast.LENGTH_SHORT).show()
    }
}
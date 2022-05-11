package com.example.tasks.profile

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
import com.example.tasks.databinding.ActivityProfileViewBinding
import java.util.*

@Suppress("DEPRECATION")
class ProfileViewActivity : AppCompatActivity() {

    private lateinit var baseImage : String
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityProfileViewBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.profileMenuDrawerLayout, R.string.open, R.string.close)
        binding.profileMenuDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.profileMenuNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btn_edit_menu -> edit()
                R.id.btn_exit_profile -> exitProfile()
            }
            true
        }

        val bundle = intent.extras
        binding.firstNameTextView.text = bundle!!.getString("firstName")
        binding.lastNameTextView.text = bundle.getString("lastName")
        binding.genderTextView.text = bundle.getString("gender")
        binding.dobTextView.text = bundle.getString("dob")
        binding.emailTextView.text = bundle.getString("email")
        binding.phoneTextView.text = bundle.getString("phone")
        baseImage = bundle.getString("image").toString()

        binding.userImageView.setImageBitmap(base64ToBitmap(baseImage))

        Toast.makeText(this, "Fetched Successfully!", Toast.LENGTH_SHORT).show()
    }

//    // Creates menu option
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.edit_menu_item, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId){
//            R.id.btn_edit_menu -> {
//                val intent = Intent(this, ProfileUpdateActivity::class.java)
//                startActivity(intent)
//                val emailId = email_text_view.text.toString()
//                val intentEmail = Intent(this@ProfileViewActivity, ProfileUpdateActivity::class.java)
//                intentEmail.putExtra("emailId",emailId)
//                intentEmail.putExtra("baseImage",baseImage)
//                startActivity(intentEmail)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

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
        val emailId = binding.emailTextView.text.toString()
        val intentEmail = Intent(this@ProfileViewActivity, ProfileUpdateActivity::class.java)
        intentEmail.putExtra("emailId",emailId)
        intentEmail.putExtra("baseImage",baseImage)
        startActivity(intentEmail)

        Toast.makeText(this, "Edit button clicked", Toast.LENGTH_SHORT).show()
    }

    private fun exitProfile() {
        val intent = Intent(this@ProfileViewActivity, ProfileLogActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Exit button clicked", Toast.LENGTH_SHORT).show()
    }
}
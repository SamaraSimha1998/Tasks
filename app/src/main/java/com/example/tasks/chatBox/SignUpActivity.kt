package com.example.tasks.chatBox

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.example.tasks.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var baseImage: String
    private val requestImageCapture = 1
    private val sharedLoginFile = "loginDetails"
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedLoginFile,
            Context.MODE_PRIVATE)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.chatBoxProfileImage.setOnClickListener { takePictureIntent() }

        binding.btnSignUp.setOnClickListener {
            val name = binding.chatBoxNameEditText.text.toString()
            val email = binding.chatBoxEmailEditText.text.toString()
            val phone = binding.chatBoxNumberEditText.text.toString()
            val password = binding.chatBoxPasswordEditText.text.toString()
            val profileImage = baseImage

            val sharedEmail: String = email
            val sharedPassword: String = password

            // Feeds data into sharedPreference file from signUp page
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("sharedEmail",sharedEmail)
            editor.putString("sharedPassword",sharedPassword)
            editor.apply()

            signUp(name, email, password, profileImage, phone)
        }
    }

    private fun signUp(name: String, email: String, password: String, profile: String, phone: String){
        // logic of creating user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@SignUpActivity, ChatActivity::class.java)
                    finish()
                    startActivity(intent)
                    addUserToDatabase(name, email, mAuth.currentUser!!.uid, profile, phone)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUpActivity, "Sign-up Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Adds data to user database
    private fun addUserToDatabase(name: String, email: String, uid: String, profile: String, phone: String){
        val chatUser = ChatBoxUser(name, email, uid, profile, phone)
        database = FirebaseDatabase.getInstance().getReference("ChatUsers")
        database.child(uid).setValue(chatUser)
    }

    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, requestImageCapture)
        } catch (e: ActivityNotFoundException) {
        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        baseImage = if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            binding.chatBoxProfileImage.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }else {
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.aapoon_logo)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            binding.chatBoxProfileImage.setImageBitmap(imageBitmap)
            val byte : ByteArray = byteArrayOutputStream.toByteArray()
            Base64.getEncoder().encodeToString(byte)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
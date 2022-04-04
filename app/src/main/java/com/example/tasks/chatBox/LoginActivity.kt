package com.example.tasks.chatBox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val sharedLoginFile = "loginDetails"
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedLoginFile,
            Context.MODE_PRIVATE)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.btnLogSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogLogin.setOnClickListener {
            val email = binding.logBoxEmailEditText.text.toString()
            val password = binding.logBoxPasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                val sharedEmail: String = email
                val sharedPassword: String = password

                // Feeds data into sharedPreference file from login page
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("sharedEmail",sharedEmail)
                editor.putString("sharedPassword",sharedPassword)
                editor.apply()
                login(email, password)
            }else{
                Toast.makeText(this, "Please enter login details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String){
        // logic for login user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@LoginActivity, ChatActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LoginActivity, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
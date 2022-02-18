package com.example.tasks.chatBox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val sharedLoginFile = "loginDetails"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedLoginFile,
            Context.MODE_PRIVATE)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        btn_log_sign_up.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btn_log_login.setOnClickListener {
            val email = log_box_email_edit_text.text.toString()
            val password = log_box_password_edit_text.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                val sharedEmail: String = email
                val sharedPassword: String = password
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
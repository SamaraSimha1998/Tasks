package com.example.tasks.chatBox

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        btn_sign_up.setOnClickListener {
            val name = chat_box_name_edit_text.text.toString()
            val email = chat_box_email_edit_text.text.toString()
            val password = chat_box_password_edit_text.text.toString()

            signUp(name, email, password)
        }
    }

    private fun signUp(name: String, email: String, password: String){
        // logic of creating user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@SignUpActivity, ChatActivity::class.java)
                    finish()
                    startActivity(intent)
                    addUserToDatabase(name, email, mAuth.currentUser!!.uid)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUpActivity, "Sign-up Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String){
        val chatUser = ChatBoxUser(name, email, uid)
        database = FirebaseDatabase.getInstance().getReference("ChatUsers")
        database.child(uid).setValue(chatUser)
    }
}
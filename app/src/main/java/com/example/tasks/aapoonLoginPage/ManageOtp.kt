package com.example.tasks.aapoonLoginPage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity
import com.example.tasks.databinding.ActivityManageOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.*
import java.util.concurrent.TimeUnit


class ManageOtp : AppCompatActivity() {

    private lateinit var phoneNumber: String
    private lateinit var otpId: String
    private var auth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val sharedAppLoginNumber = "loginNumber"
    private lateinit var binding: ActivityManageOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.getStringExtra("phoneNumber").toString()

        initiateOtp()

        binding.btnVerifySignin.setOnClickListener {
            when {
                binding.enterOtpEditText.text.toString().isEmpty() -> {
                    Toast.makeText(this,"Please enter OTP!",Toast.LENGTH_SHORT).show()
                }
                binding.enterOtpEditText.text.toString().length != 6 -> {
                    Toast.makeText(this,"Please enter valid OTP!",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(otpId,binding.enterOtpEditText.text.toString())
                    signInWithPhoneAuthCredential(credential)
                }
            }
        }
    }

    private fun initiateOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            this,  // Activity (for callback binding)
            object : OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    otpId = s
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }
            }) // OnVerificationStateChangedCallbacks
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedAppLoginNumber, Context.MODE_PRIVATE)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    signIn()
                    val sharedPhoneNumber: String = phoneNumber

                    // Feeds data into sharedPreference file from login page
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("sharedPhoneNumber",sharedPhoneNumber)
                    editor.apply()
                    Toast.makeText(applicationContext, "OTP verified", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Signin Code Error", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun signIn() {
        database = FirebaseDatabase.getInstance().getReference("AppProfiles")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(phoneNumber)) {
                    val intent = Intent(this@ManageOtp, DashBoardActivity::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@ManageOtp, UserCategories::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Signin Code Error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this, PhoneNumberVerification::class.java)
        startActivity(intent)
        finish()
    }
}
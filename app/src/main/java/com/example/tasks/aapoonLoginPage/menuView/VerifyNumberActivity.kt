package com.example.tasks.aapoonLoginPage.menuView

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity
import com.example.tasks.databinding.ActivityVerifyNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import java.util.concurrent.TimeUnit

class VerifyNumberActivity : AppCompatActivity() {

    private lateinit var phoneNumber: String
    private lateinit var otpId: String
    private var auth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityVerifyNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.getStringExtra("updatePhoneNumber").toString()

        initiateOtp()

        binding.btnVerifyOtp.setOnClickListener {
            when {
                binding.verifyOtpEditText.text.toString().isEmpty() -> {
                    Toast.makeText(this,"Please enter OTP!", Toast.LENGTH_SHORT).show()
                }
                binding.verifyOtpEditText.text.toString().length != 6 -> {
                    Toast.makeText(this,"Please enter valid OTP!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(otpId,binding.verifyOtpEditText.text.toString())
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
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
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
        Log.d("creden", credential.toString())
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    database = FirebaseDatabase.getInstance().getReference("AppProfiles")
                    database.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.hasChild(phoneNumber)) {
                                val intent = Intent(this@VerifyNumberActivity, DashBoardActivity::class.java)
                                intent.putExtra("phoneNumber", phoneNumber)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@VerifyNumberActivity, DashBoardActivity::class.java)
                                intent.putExtra("phoneNumber", phoneNumber)
                                startActivity(intent)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "Verification Error", Toast.LENGTH_LONG).show()
                        }
                    })
                    Toast.makeText(applicationContext, "OTP verified", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Verification Error", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashBoardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.example.tasks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.aapoonLoginPage.PhoneNumberVerification
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity
import com.example.tasks.chatBox.ChatActivity
import com.example.tasks.chatBox.LoginActivity
import com.example.tasks.contacts.SendMessageActivity
import com.example.tasks.databinding.ActivityMainBinding
import com.example.tasks.dependencyInjection.dagger.DaggerActivity
import com.example.tasks.fcm.BatteryIndicatorNotificationActivity
import com.example.tasks.glide.GlideImageCropper
import com.example.tasks.imageCompressor.ImageCompressorActivity
import com.example.tasks.imageslider.ImageSliderActivity
import com.example.tasks.jobscheduler.JobScheduler
import com.example.tasks.mediaplayer.VideoListActivity
import com.example.tasks.multiThreading.MultiThreading
import com.example.tasks.nativeImageCompressor.NativeImageCompressorActivity
import com.example.tasks.profile.ProfileLogActivity
import com.example.tasks.realm.RealmDemo
import com.example.tasks.realm.SharedData
import com.example.tasks.tabProfile.TabProfileActivity
import com.example.tasks.validatingUSADLN.DetailsActivity
import com.example.tasks.webretrofit.WebApiRetrofit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val sharedLoginFile = "loginDetails"
    private val sharedAppLoginNumber = "loginNumber"
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedLoginFile,
            Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        val sharedNumber: SharedPreferences = this.getSharedPreferences(sharedAppLoginNumber,
            Context.MODE_PRIVATE)

        FirebaseMessaging.getInstance().subscribeToTopic("aapoon")

        binding.btnImageSlider.setOnClickListener {
            val intent = Intent(this@MainActivity, ImageSliderActivity::class.java)
            startActivity(intent)
        }

        binding.btnWebApi.setOnClickListener {
            val intent = Intent(this@MainActivity, WebApiRetrofit::class.java)
            startActivity(intent)
        }

        binding.btnSharedData.setOnClickListener {
            val intent = Intent(this@MainActivity, SharedData::class.java)
            startActivity(intent)
        }

        binding.btnRealmData.setOnClickListener {
            val intent = Intent(this@MainActivity, RealmDemo::class.java)
            startActivity(intent)
        }

        binding.btnJobScheduler.setOnClickListener {
            val intent = Intent(this@MainActivity, JobScheduler::class.java)
            startActivity(intent)
        }

        binding.btnGlide.setOnClickListener {
            val intent = Intent(this@MainActivity, GlideImageCropper::class.java)
            startActivity(intent)
        }

        binding.btnMediaPlayer.setOnClickListener {
            val intent = Intent(this@MainActivity, VideoListActivity::class.java)
            startActivity(intent)
        }

        binding.btnSms.setOnClickListener {
            val intent = Intent(this@MainActivity, SendMessageActivity::class.java)
            startActivity(intent)
        }

        binding.btnProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileLogActivity::class.java)
            startActivity(intent)
        }

        binding.btnTabViewProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, TabProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnNativeImageCompressor.setOnClickListener {
            val intent = Intent(this@MainActivity, NativeImageCompressorActivity::class.java)
            startActivity(intent)
        }

        binding.btnImageCompressor.setOnClickListener {
            val intent = Intent(this@MainActivity, ImageCompressorActivity::class.java)
            startActivity(intent)
        }

        binding.btnMultiThreading.setOnClickListener {
            val intent = Intent(this@MainActivity, MultiThreading::class.java)
            startActivity(intent)
        }

        binding.btnChatBox.setOnClickListener {
            val sharedEmail = sharedPreferences.getString("sharedEmail",null)
            val sharedPassword = sharedPreferences.getString("sharedPassword",null)

            // Checks is there any login data saved or not in sharedPreference file
            if(sharedEmail == null && sharedPassword == null) {
                // Login page
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            } else if (sharedEmail != null && sharedPassword != null) {
                    login(sharedEmail,sharedPassword)
            }
        }

        binding.btnAapoonLogin.setOnClickListener {
            val sharedPhoneNumber = sharedNumber.getString("sharedPhoneNumber", null)

            // Checks weather user already verified number or not
            if (sharedPhoneNumber == null) {
                // Number Verification page
                val intent = Intent(this@MainActivity, PhoneNumberVerification::class.java)
                startActivity(intent)
            } else {
                // Direct to dashboard
                signIn(sharedPhoneNumber)
            }
        }

        binding.btnDlnDetails.setOnClickListener {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            startActivity(intent)
        }

        binding.btnBatteryIndicator.setOnClickListener {
            val intent = Intent(this@MainActivity, BatteryIndicatorNotificationActivity::class.java)
            startActivity(intent)
        }

        binding.btnDaggerDi.setOnClickListener {
            val intent = Intent(this@MainActivity, DaggerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String){
        // logic for login user
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@MainActivity, ChatActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    //Toast.makeText(this@MainActivity, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn(phoneNumber: String) {
        database = FirebaseDatabase.getInstance().getReference("AppProfiles")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(phoneNumber)) {
                    val intent = Intent(this@MainActivity, DashBoardActivity::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                } else {
                    // Open signup page.
                    val intent = Intent(this@MainActivity, PhoneNumberVerification::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Signin Error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }
}
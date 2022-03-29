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
import com.example.tasks.fcm.BatteryIndicatorNotificationActivity
import com.example.tasks.glide.GlideImageCropper
import com.example.tasks.imageCompressor.ImageCompressorActivity
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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val sharedLoginFile = "loginDetails"
    private val sharedAppLoginNumber = "loginNumber"
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedLoginFile,
            Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        val sharedNumber: SharedPreferences = this.getSharedPreferences(sharedAppLoginNumber,
            Context.MODE_PRIVATE)

        FirebaseMessaging.getInstance().subscribeToTopic("aapoon")

        btn_web_api.setOnClickListener {
            val intent = Intent(this@MainActivity, WebApiRetrofit::class.java)
            startActivity(intent)
        }

        btn_shared_data.setOnClickListener {
            val intent = Intent(this@MainActivity, SharedData::class.java)
            startActivity(intent)
        }

        btn_realm_data.setOnClickListener {
            val intent = Intent(this@MainActivity, RealmDemo::class.java)
            startActivity(intent)
        }

        btn_job_scheduler.setOnClickListener {
            val intent = Intent(this@MainActivity, JobScheduler::class.java)
            startActivity(intent)
        }

        btn_glide.setOnClickListener {
            val intent = Intent(this@MainActivity, GlideImageCropper::class.java)
            startActivity(intent)
        }

        btn_media_player.setOnClickListener {
            val intent = Intent(this@MainActivity, VideoListActivity::class.java)
            startActivity(intent)
        }

        btn_sms.setOnClickListener {
            val intent = Intent(this@MainActivity, SendMessageActivity::class.java)
            startActivity(intent)
        }

        btn_profile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileLogActivity::class.java)
            startActivity(intent)
        }

        btn_tab_view_profile.setOnClickListener {
            val intent = Intent(this@MainActivity, TabProfileActivity::class.java)
            startActivity(intent)
        }

        btn_native_image_compressor.setOnClickListener {
            val intent = Intent(this@MainActivity, NativeImageCompressorActivity::class.java)
            startActivity(intent)
        }

        btn_image_compressor.setOnClickListener {
            val intent = Intent(this@MainActivity, ImageCompressorActivity::class.java)
            startActivity(intent)
        }

        btn_multi_threading.setOnClickListener {
            val intent = Intent(this@MainActivity, MultiThreading::class.java)
            startActivity(intent)
        }

        btn_chat_box.setOnClickListener {
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

        btn_aapoon_login.setOnClickListener {
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

        btn_dln_details.setOnClickListener {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            startActivity(intent)
        }

        btn_battery_indicator.setOnClickListener {
            val intent = Intent(this@MainActivity, BatteryIndicatorNotificationActivity::class.java)
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
package com.example.tasks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.chatBox.ChatActivity
import com.example.tasks.chatBox.LoginActivity
import com.example.tasks.contacts.SendMessageActivity
import com.example.tasks.glide.GlideImageCropper
import com.example.tasks.imageCompressor.ImageCompressorActivity
import com.example.tasks.jobscheduler.JobScheduler
import com.example.tasks.mediaplayer.VideoListActivity
import com.example.tasks.multiThreading.MultiThreading
import com.example.tasks.nativeImageCompressor.NativeImageCompressorActivity
import com.example.tasks.profile.ProfileLogActivity
import com.example.tasks.realm.RealmDemo
import com.example.tasks.realm.SharedData
import com.example.tasks.sideNavMenu.MenuActivity
import com.example.tasks.tabProfile.TabProfileActivity
import com.example.tasks.webretrofit.WebApiRetrofit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val sharedLoginFile = "loginDetails"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedLoginFile,
            Context.MODE_PRIVATE)
        mAuth = FirebaseAuth.getInstance()

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

        btn_menu_bar.setOnClickListener {
            val intent = Intent(this@MainActivity, MenuActivity::class.java)
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
            }else {
                if (sharedEmail != null && sharedPassword != null) {
                    login(sharedEmail,sharedPassword)
                }
            }
        }
    }

    private fun login(email: String, password: String){
        // logic for login user
        mAuth.signInWithEmailAndPassword(email, password)
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
}
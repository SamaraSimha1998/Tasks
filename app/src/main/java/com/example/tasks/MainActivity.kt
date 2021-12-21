package com.example.tasks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_web_api_retrofit.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().subscribeToTopic("Aapoon");

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
    }
}
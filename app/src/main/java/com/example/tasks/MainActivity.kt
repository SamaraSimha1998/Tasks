package com.example.tasks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.contacts.ContactsActivity
import com.example.tasks.contacts.SendMessageActivity
import com.example.tasks.glide.GlideImageCropper
import com.example.tasks.jobscheduler.JobScheduler
import com.example.tasks.mediaplayer.VideoListActivity
import com.example.tasks.realm.RealmDemo
import com.example.tasks.realm.SharedData
import com.example.tasks.webretrofit.WebApiRetrofit
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_web_api_retrofit.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }
}
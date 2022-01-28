package com.example.tasks.mediaplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import java.util.*

class VideoListActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        if (checkPermission()) {
            videoList()
        }
    }

    // Feeds videos to recycler list
    private fun videoList() {
        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        recyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        videoArrayList = ArrayList<VideoModel>()
        videos
    }

    // Reads video data with required fields like time, resolution
    private val videos: Unit
        @SuppressLint("Range", "Recycle")
        get() {
            val contentResolver = contentResolver
            val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val cursor = contentResolver.query(uri, null, null, null, null)

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val duration =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                    val data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val videoModel = VideoModel()
                    videoModel.videoTitle
                    videoModel.videoUri
                    videoModel.videoDuration
                    videoArrayList!!.add(videoModel)
                } while (cursor.moveToNext())
            }
            val adapter = videoArrayList?.let { VideoAdapter(this, it) }
            recyclerView!!.adapter = adapter
            adapter?.setOnItemClickListener(object : VideoAdapter.OnItemClickListener {
                override fun onItemClick(pos: Int, v: View?) {
                    val intent = Intent(applicationContext, VideoPlayActivity::class.java)
                    intent.putExtra("pos", pos)
                    startActivity(intent)
                }
            })
        }

    //Converts time into required format for video view
    private fun timeConversion(value: Long): String {
        val videoTime: String
        val dur = value.toInt()
        val hrs = dur / 3600000
        val mns = dur / 60000 % 60000
        val scs = dur % 60000 / 1000
        videoTime = if (hrs > 0) {
            String.format("%02d:%02d:%02d", hrs, mns, scs)
        } else {
            String.format("%02d:%02d", mns, scs)
        }
        return videoTime
    }

    // Access for External storage to read data
    private fun checkPermission(): Boolean {
        val readExternalPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (readExternalPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_READ)
            return false
        }
        return true
    }

    // Checks weather permission granted or not
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_READ -> {
                if (grantResults.isNotEmpty() && permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(applicationContext,
                            "Please allow storage permission",
                            Toast.LENGTH_LONG).show()
                    } else {
                        videoList()
                    }
                }
            }
        }
    }

    companion object {
        var videoArrayList: ArrayList<VideoModel>? = null
        const val PERMISSION_READ = 0
    }
}
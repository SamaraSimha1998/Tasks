package com.example.tasks.mediaplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tasks.R
import com.example.tasks.mediaplayer.VideoListActivity.Companion.videoArrayList

class VideoPlayActivity : AppCompatActivity() {
    var videoView: VideoView? = null
    private var prev: ImageView? = null
    var next: ImageView? = null
    private var pause: ImageView? = null
    var seekBar: SeekBar? = null
    private var videoIndex = 0
    var currentPos = 0.0
    private var totalDuration = 0.0
    var current: TextView? = null
    private var total: TextView? = null
    private var showProgress: LinearLayout? = null
    private var mHandler: Handler? = null
    private var handler: Handler? = null
    private var isVisible: Boolean? = true
    private var relativeLayout: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        if (checkPermission()) {
            setVideo()
        }
    }

    // Sets selected video to UI of play activity
    private fun setVideo() {
        videoView = findViewById<View>(R.id.videoView) as VideoView
        prev = findViewById<View>(R.id.prev) as ImageView
        next = findViewById<View>(R.id.next) as ImageView
        pause = findViewById<View>(R.id.pause) as ImageView
        seekBar = findViewById<View>(R.id.seekbar) as SeekBar
        current = findViewById<View>(R.id.current) as TextView
        total = findViewById<View>(R.id.total) as TextView
        showProgress = findViewById<View>(R.id.showProgress) as LinearLayout
        relativeLayout = findViewById<View>(R.id.relative) as RelativeLayout
        videoIndex = intent.getIntExtra("pos", 0)
        mHandler = Handler()
        handler = Handler()
        videoView!!.setOnCompletionListener {
            videoIndex++
            if (videoIndex < videoArrayList!!.size) {
                playVideo(videoIndex)
            } else {
                videoIndex = 0
                playVideo(videoIndex)
            }
        }
        videoView!!.setOnPreparedListener { setVideoProgress() }
        playVideo(videoIndex)
        prevVideo()
        nextVideo()
        setPause()
        hideLayout()
    }

    // plays selected video
    private fun playVideo(pos: Int) {
        try {
            videoView!!.setVideoURI(videoArrayList?.get(pos)?.videoUri)
            videoView!!.start()
            pause!!.setImageResource(R.drawable.ic_pause)
            videoIndex = pos
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Shows current progress of video
    private fun setVideoProgress() {
        currentPos = videoView!!.currentPosition.toDouble()
        totalDuration = videoView!!.duration.toDouble()

        total!!.text = timeConversion(totalDuration.toLong())
        current!!.text = timeConversion(currentPos.toLong())
        seekBar!!.max = totalDuration.toInt()
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                try {
                    currentPos = videoView!!.currentPosition.toDouble()
                    current!!.text = timeConversion(currentPos.toLong())
                    seekBar!!.progress = currentPos.toInt()
                    handler.postDelayed(this, 1000)
                } catch (ed: IllegalStateException) {
                    ed.printStackTrace()
                }
            }
        }
        handler.postDelayed(runnable, 1000)

        // Seekbar runs position of video
        seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                currentPos = seekBar.progress.toDouble()
                videoView!!.seekTo(currentPos.toInt())
            }
        })
    }

    // Gets to previous video from current video
    private fun prevVideo() {
        prev!!.setOnClickListener {
            if (videoIndex > 0) {
                videoIndex--
                playVideo(videoIndex)
            } else {
                videoIndex = (videoArrayList?.size ?: -1)
                playVideo(videoIndex)
            }
        }
    }

    // Gets to next video from current video
    private fun nextVideo() {
        next!!.setOnClickListener {
            if (videoIndex < (videoArrayList?.size ?: -1)) {
                videoIndex++
                playVideo(videoIndex)
            } else {
                videoIndex = 0
                playVideo(videoIndex)
            }
        }
    }

    // Play & Pause options are operated from this function
    private fun setPause() {
        pause!!.setOnClickListener {
            if (videoView!!.isPlaying) {
                videoView!!.pause()
                pause!!.setImageResource(R.drawable.ic_play)
            } else {
                videoView!!.start()
                pause!!.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    fun timeConversion(value: Long): String {
        val songTime: String
        val dur = value.toInt()
        val hrs = dur / 3600000
        val mns = dur / 60000 % 60000
        val scs = dur % 60000 / 1000
        songTime = if (hrs > 0) {
            String.format("%02d:%02d:%02d", hrs, mns, scs)
        } else {
            String.format("%02d:%02d", mns, scs)
        }
        return songTime
    }

    // Hides the options layout on screen after start playing video
    private fun hideLayout() {
        val runnable = Runnable {
            showProgress!!.visibility = View.GONE
            isVisible = false
        }
        handler!!.postDelayed(runnable, 5000)
        relativeLayout!!.setOnClickListener {
            mHandler!!.removeCallbacks(runnable)
            if (isVisible == true) {
                showProgress!!.visibility = View.GONE
                isVisible = false
            } else {
                showProgress!!.visibility = View.VISIBLE
                mHandler!!.postDelayed(runnable, 5000)
                isVisible = true
            }
        }
    }

    // Access to External storage to read data
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

    // Reads video files from External storage after giving access
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
                        setVideo()
                    }
                }
            }
        }
    }

    companion object {
        const val PERMISSION_READ = 0
    }
}
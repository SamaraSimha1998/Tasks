package com.example.tasks.videoPlayer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.tasks.R
import com.example.tasks.databinding.ActivityVideoPlayerBinding
import com.example.tasks.videoPlayer.adapter.VideoPlayerRecyclerAdapter
import com.example.tasks.videoPlayer.model.MediaObject
import com.example.tasks.videoPlayer.utils.VerticalSpacingItemDecorator
import java.util.*
import kotlin.collections.ArrayList

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVideos()
    }

    private fun initVideos() {
        val layoutManager = LinearLayoutManager(this)
        binding.videoPlayerRecyclerView.layoutManager = layoutManager
        val verticalItemDecor = VerticalSpacingItemDecorator(10)
        binding.videoPlayerRecyclerView.addItemDecoration(verticalItemDecor)

        val sourceVideos: ArrayList<MediaObject> = ArrayList(sampleVideoList())
//        binding.videoPlayerRecyclerView.MediaObject(sourceVideos)
        val adapter = initGlide()?.let { VideoPlayerRecyclerAdapter(sourceVideos, it) }
        binding.videoPlayerRecyclerView.adapter = adapter
    }

    private fun initGlide(): RequestManager? {
        val options = RequestOptions()
            .placeholder(R.color.white)
            .error(R.color.white)
        return Glide.with(this).setDefaultRequestOptions(options)
    }

    private fun sampleVideoList(): List<MediaObject>? {
        return listOf(
            MediaObject("Big Buck Bunny",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                "https://i.ytimg.com/vi/aqz-KE-bpKQ/maxresdefault.jpg",
                "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org")
        )
    }
}
package com.example.tasks.videoPlayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tasks.R
import com.example.tasks.videoPlayer.VideoPlayerViewHolder
import com.example.tasks.videoPlayer.model.MediaObject

class VideoPlayerRecyclerAdapter(
    mediaObjects: ArrayList<MediaObject>,
    requestManager: RequestManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mediaObjects: ArrayList<MediaObject>
    private val requestManager: RequestManager
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return VideoPlayerViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.layout_video_list_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        (viewHolder as VideoPlayerViewHolder).onBind(mediaObjects[i], requestManager)
    }

    override fun getItemCount(): Int {
        return mediaObjects.size
    }

    init {
        this.mediaObjects = mediaObjects
        this.requestManager = requestManager
    }
}


package com.example.tasks.videoPlayer

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tasks.R
import com.example.tasks.videoPlayer.model.MediaObject

class VideoPlayerViewHolder(var parent: View) : RecyclerView.ViewHolder(
    parent
) {
    var media_container: FrameLayout
    var title: TextView
    var thumbnail: ImageView
    var volumeControl: ImageView
    var progressBar: ProgressBar
    var requestManager: RequestManager? = null
    fun onBind(mediaObject: MediaObject, requestManager: RequestManager?) {
        this.requestManager = requestManager
        parent.tag = this
        title.text = mediaObject.title
        this.requestManager
            ?.load(mediaObject.thumbnail)
            ?.into(thumbnail)
    }

    init {
        media_container = itemView.findViewById(R.id.media_container)
        thumbnail = itemView.findViewById(R.id.thumbnail)
        title = itemView.findViewById(R.id.title)
        progressBar = itemView.findViewById(R.id.progressBar)
        volumeControl = itemView.findViewById(R.id.volume_control)
    }
}
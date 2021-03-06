package com.example.tasks.mediaplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView

@Suppress("DEPRECATION")
class VideoAdapter(var context: Context, private var videoArrayList: ArrayList<VideoModel>) :
    RecyclerView.Adapter<VideoAdapter.viewHolder>() {
    var onItemClickListener: Any? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): viewHolder {
        val view: View = LayoutInflater.from(context).inflate(com.example.tasks.R.layout.activity_video_list, viewGroup, false)
        return viewHolder(view)
    }


    override fun onBindViewHolder(holder: viewHolder, i: Int) {
        holder.title.text = videoArrayList[i].videoTitle
        holder.duration.text = videoArrayList[i].videoDuration
    }

    override fun getItemCount(): Int {
        return videoArrayList.size
    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(com.example.tasks.R.id.title) as TextView
        var duration: TextView = itemView.findViewById<View>(com.example.tasks.R.id.duration) as TextView

        init {
            itemView.setOnClickListener { v ->
                onItemClickListener.onItemClick(adapterPosition, v)
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int, v: View?)
    }
}

private fun Any?.onItemClick(pos: Int, v: View?) {

    val videoView = (com.example.tasks.R.id.videoView) as VideoView
    val mediaController = MediaController(this as Context?)
    mediaController.setAnchorView(videoView)
    videoView.setMediaController(mediaController)
    videoView.setVideoURI(Uri.parse(this as String?))
    videoView.start()
}

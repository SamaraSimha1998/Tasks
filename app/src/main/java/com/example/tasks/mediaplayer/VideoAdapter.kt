package com.example.tasks.mediaplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import java.util.ArrayList


class VideoAdapter(var context: Context, var videoArrayList: ArrayList<VideoModel>) :
    RecyclerView.Adapter<VideoAdapter.viewHolder>() {
    var onItemClickListener: Any? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): viewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_video_list, viewGroup, false)
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
        var title: TextView = itemView.findViewById<View>(R.id.title) as TextView
        var duration: TextView = itemView.findViewById<View>(R.id.duration) as TextView

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
    TODO("Not yet implemented")
}

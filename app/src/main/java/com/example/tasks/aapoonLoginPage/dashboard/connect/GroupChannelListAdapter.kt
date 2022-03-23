package com.example.tasks.aapoonLoginPage.dashboard.connect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.aapoonLoginPage.dashboard.chat.ChattingActivity

class GroupChannelListAdapter(list: List<GroupChannel>, channelType: String) :
    RecyclerView.Adapter<GroupChannelListAdapter.GroupChannelListViewHolder>() {
    private val mList: List<GroupChannel> = list
    private val mChannelType: String = channelType

    inner class GroupChannelListViewHolder(v: LinearLayout?) : RecyclerView.ViewHolder(
        v!!
    ) {
        var channelUrlTextView: TextView = itemView.findViewById(R.id.channel_url_text_view)
        var joinChannelButton: Button = itemView.findViewById(R.id.join_channel_button)
        var channelUrl: String? = null

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupChannelListViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.channel_item, parent, false) as LinearLayout
        return GroupChannelListViewHolder(v)
    }

    override fun onBindViewHolder(holder: GroupChannelListViewHolder, position: Int) {
        holder.channelUrl = mList[position].getUrl()
        holder.channelUrlTextView.text = holder.channelUrl
        val channelUrl = holder.channelUrl
        holder.joinChannelButton.setOnClickListener { v ->
            val context: Context = v.context
            val intent = Intent(context, ChattingActivity::class.java)
            val bundle = Bundle()
            bundle.putString("channelUrl", channelUrl)
            bundle.putString("channelType", mChannelType)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mList.size
    }

}
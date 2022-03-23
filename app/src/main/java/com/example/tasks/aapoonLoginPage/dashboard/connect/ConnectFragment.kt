package com.example.tasks.aapoonLoginPage.dashboard.connect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.tasks.R


class ConnectFragment(val uid: String): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_connect, container, false)
        // connection for groups and personal chats are shown here
        val groupChannelList: LinearLayout =
            view.findViewById(R.id.group_account_linear_layout)
        groupChannelList.setOnClickListener {
            val intent = Intent(context, ChannelListActivity::class.java)
            intent.putExtra("userID", uid)
            intent.putExtra("channelType", Constants.groupChannelType)
            startActivity(intent)
        }

        val personalChannelList: LinearLayout =
            view.findViewById(R.id.personal_account_linear_layout)
        personalChannelList.setOnClickListener {
            val intent = Intent(context, ChannelListActivity::class.java)
            intent.putExtra("userID", uid)
            intent.putExtra("channelType", Constants.privateChannelType)
            startActivity(intent)
        }

        return view
    }
}
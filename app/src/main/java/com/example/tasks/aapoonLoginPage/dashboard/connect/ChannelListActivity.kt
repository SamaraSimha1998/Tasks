package com.example.tasks.aapoonLoginPage.dashboard.connect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R

class ChannelListActivity : AppCompatActivity() {

    private var userId: String? = null
    private var channelType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)

        userId = intent.getStringExtra("userID")
        channelType = intent.getStringExtra("channelType")

        init_sendbird()
    }

    override fun onResume() {
        super.onResume()
        connect_sendbird()
        when (channelType) {
            Constants.privateChannelType -> getPrivateChannels()
            Constants.groupChannelType -> getGroupChannels()
            else -> {
                Log.e("App", "Invalid Channel Type: $channelType")
                finish()
            }
        }
    }

     private fun getGroupChannels() {

         val intent = Intent(this, GroupChannelActivity::class.java)
         startActivity(intent)
//        val channelListQuery: GroupChannelListQuery = GroupChannel.createMyGroupChannelListQuery()
//        channelListQuery.setIncludeEmpty(true)
//        channelListQuery.setLimit(100)
//        channelListQuery.next(object : GroupChannelListQueryResultHandler() {
//            fun onResult(list: List<GroupChannel?>?, e: SendBirdException?) {
//                if (e != null) {    // Error.
//                    return
//                }
//                populate_group_channel_list(list)
//            }
//        })
    }

     private fun getPrivateChannels() {

         val intent = Intent(this, PrivateChannelActivity::class.java)
         startActivity(intent)
//        val channelListQuery: PrivateChannelListQuery = PrivateChannel.createOpenChannelListQuery()
//        channelListQuery.setLimit(100)
//        channelListQuery.next(object : PrivateChannelListQueryResultHandler() {
//            fun onResult(list: List<PrivateChannel?>?, e: SendBirdException?) {
//                if (e != null) {    // Error.
//                    return
//                }
//                populate_open_channel_list(list)
//            }
//        })
    }

     fun populate_group_channel_list(list: List<GroupChannel?>?) {
        val rvGroupChannelList = findViewById<RecyclerView>(R.id.accounts_list_recycler_view)
        val adapter = list?.let { channelType?.let { it1 -> GroupChannelListAdapter(it as List<GroupChannel>, it1) } }
        rvGroupChannelList.adapter = adapter
        rvGroupChannelList.layoutManager = LinearLayoutManager(this)
    }

     fun populate_open_channel_list(list: List<PrivateChannel?>?) {
        val rvPrivateChannelList = findViewById<RecyclerView>(R.id.accounts_list_recycler_view)
        val adapter = list?.let { channelType?.let { it1 -> PrivateChannelListAdapter(it as List<PrivateChannel>, it1) } }
        rvPrivateChannelList.adapter = adapter
        rvPrivateChannelList.layoutManager = LinearLayoutManager(this)
    }

     private fun connect_sendbird() {
//        SendBird.connect(userId, object : ConnectHandler() {
//            fun onConnected(user: User?, e: SendBirdException?) {
//                if (e != null) {    // Error.
//                    return
//                }
//            }
//        })
    }

     private fun init_sendbird() {
//        SendBird.init(userId, this)
    }
}
package com.example.tasks.chatBox

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.databinding.ActivityChatBoxBinding
import com.example.tasks.fcm.channelId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatBoxActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var database: DatabaseReference
    private var simpleDateFormat: SimpleDateFormat? = null
    private var calendar: Calendar? = null
    private var currentTime: String? = null
    private lateinit var binding: ActivityChatBoxBinding

    // Creates a unique room as for privacy
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    @SuppressLint("UnspecifiedImmutableFlag", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("hh:mm a")

        database = FirebaseDatabase.getInstance().getReference("Chats")

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        binding.chatBoxRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatBoxRecyclerView.adapter = messageAdapter

        // Logic for adding messages data to recycler view
        database.child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                    if(messageList.size == 0){
                        // skip
                    }else{
                        binding.chatBoxRecyclerView.smoothScrollToPosition(messageList.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        // Adding the message to database
        binding.sendImageView.setOnClickListener {
            val message = binding.chatMessageBox.text.toString()
//            val date = Date()
//            var currentTime = simpleDateFormat!!.format(calendar!!.time)
//            val messageObject = Message(message, senderUid, date.time, currentTime)

            // push() creates unique node every time it calls
//            if (message != "") {
//                notification(message)
//                database.child(senderRoom!!).child("messages").push()
//                    .setValue(messageObject).addOnSuccessListener {
//                        database.child(receiverRoom!!).child("messages").push()
//                            .setValue(messageObject)
//                    }
//                binding.chatMessageBox.setText("")
//            }else {
//                Toast.makeText(this,"Empty message",Toast.LENGTH_SHORT).show()
//            }

            if (message == "") {
                Toast.makeText(applicationContext, "Enter message first", Toast.LENGTH_SHORT).show()
            } else {
                val date = Date()
                currentTime = simpleDateFormat!!.format(calendar!!.time)
                val messages = Message(message, senderUid, date.time, currentTime)
                database
                    .child(senderRoom!!)
                    .child("messages")
                    .push().setValue(messages).addOnCompleteListener {
                        database
                            .child(receiverRoom!!)
                            .child("messages")
                            .push()
                            .setValue(messages).addOnCompleteListener { notification(message) }
                    }
                binding.chatMessageBox.text = null
            }
        }
    }

    // Notification manager
    private fun notification(textContent: String) {
//        FirebaseMessagingServiceApi().sendMessageNotification("Received message", textContent)
        // Uses default notification sound for message
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(textContent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setSound(defaultSoundUri)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}
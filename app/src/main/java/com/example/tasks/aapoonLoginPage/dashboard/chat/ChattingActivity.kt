package com.example.tasks.aapoonLoginPage.dashboard.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.chatBox.Message
import com.example.tasks.databinding.ActivityChattingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChattingActivity : AppCompatActivity() {

    private lateinit var messageAdapter: ChatMessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityChattingBinding

    // Creates a unique room as for privacy
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("AapoonChats")

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        messageList = ArrayList()
        messageAdapter = ChatMessageAdapter(this, messageList)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = messageAdapter

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
                        binding.chatRecyclerView.smoothScrollToPosition(messageList.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        // Adding the message to database
        binding.sendMessageImageView.setOnClickListener {
            val message = binding.chatMessage.text.toString()
            val messageObject = Message(message, senderUid)

            // push() creates unique node every time it calls
            when {
                message != "" -> {
                    database.child(senderRoom!!).child("messages").push()
                        .setValue(messageObject).addOnSuccessListener {
                            database.child(receiverRoom!!).child("messages").push()
                                .setValue(messageObject)
                        }
                    notification()
                    binding.chatMessage.setText("")
                }
                message != " " -> {
                    Toast.makeText(this,"Empty message",Toast.LENGTH_SHORT).show()
                    binding.chatMessage.setText("")
                }
                else -> {
                    Toast.makeText(this,"Empty message",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Notification manager
    private fun notification() {}
}
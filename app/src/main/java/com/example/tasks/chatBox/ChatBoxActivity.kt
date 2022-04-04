package com.example.tasks.chatBox

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.databinding.ActivityChatBoxBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatBoxActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityChatBoxBinding

    // Creates a unique room as for privacy
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
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
            val messageObject = Message(message, senderUid)

            // push() creates unique node every time it calls
            if (message != "") {
                database.child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        database.child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                notification()
                binding.chatMessageBox.setText("")
            }else {
                Toast.makeText(this,"Empty message",Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Notification manager
    private fun notification() {}
}
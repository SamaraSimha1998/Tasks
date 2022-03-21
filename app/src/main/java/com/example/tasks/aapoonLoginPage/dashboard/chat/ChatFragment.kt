package com.example.tasks.aapoonLoginPage.dashboard.chat

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.aapoonLoginPage.AppProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment(val phoneNumber: String): Fragment() {

    private lateinit var userList: ArrayList<AppProfile>
    private lateinit var adapter: ChatAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("AppProfiles")
        userList = ArrayList()
        adapter = ChatAdapter(requireContext(), userList)
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)
        view.chat_user_recycler_view.layoutManager = LinearLayoutManager(context)
        view.chat_user_recycler_view.adapter = adapter

        database.addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged", "Range", "Recycle")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(AppProfile::class.java)

                    // Adds user without checking saved contacts or not
                    if(auth.currentUser?.uid != currentUser?.uid){
                        Log.d("currentUser",phoneNumber)
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return view
    }
}
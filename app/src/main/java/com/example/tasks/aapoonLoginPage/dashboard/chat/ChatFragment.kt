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
import com.example.tasks.aapoonLoginPage.AppProfile
import com.example.tasks.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment(val phoneNumber: String): Fragment() {

    private lateinit var userList: ArrayList<AppProfile>
    private lateinit var adapter: ChatAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("AppProfiles")
        userList = ArrayList()
        adapter = ChatAdapter(requireContext(), userList)
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.chatUserRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatUserRecyclerView.adapter = adapter

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
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
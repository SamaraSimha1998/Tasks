package com.example.tasks.chatBox

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.MainActivity
import com.example.tasks.R
import com.example.tasks.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {

    private lateinit var userList: ArrayList<ChatBoxUser>
    private lateinit var adapter: ChatBoxAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var database: DatabaseReference
    private val sharedLoginFile = "loginDetails"
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("ChatUsers")
        firebaseFirestore = FirebaseFirestore.getInstance()

        userList = ArrayList()
        adapter = ChatBoxAdapter(this, userList)

        binding.chatBoxUserRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatBoxUserRecyclerView.adapter = adapter

        database.addValueEventListener(object: ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged", "Range", "Recycle")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(ChatBoxUser::class.java)

                    // Adds user without checking saved contacts or not
//                    if(auth.currentUser?.uid != currentUser?.uid){
//                        userList.add(currentUser!!)
//                    }

                    // Adds user by checking saved contacts from mobile
                    if(auth.currentUser?.uid != currentUser?.uid){
                        val name = checkContact(currentUser?.phone)
                        if (name != null) {
                            userList.add(currentUser!!)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            // Logic for Logout
                auth.signOut()

            // Clearing data in sharedPreferences
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedLoginFile,
                Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this@ChatActivity, LoginActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }

    @SuppressLint("Range")
    fun checkContact(number: String?): String? {
        val uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        var name: String? = null
        val contentResolver = contentResolver
        val contactLookup = contentResolver.query(
            uri, arrayOf(
                BaseColumns._ID,
                PhoneLookup.DISPLAY_NAME
            ), null, null, null
        )
        try {
            if (contactLookup != null && contactLookup.count > 0) {
                contactLookup.moveToNext()
                name =
                    contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }
        } catch (e: Exception) {}
        finally {
            contactLookup?.close()
        }
        return name
    }

    override fun onStop() {
        super.onStop()
        val documentReference = firebaseFirestore.collection("ChatUsers").document(
            auth.uid!!
        )
        documentReference.update("status", "Offline").addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "Now User is Offline",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val documentReference = firebaseFirestore.collection("ChatUsers").document(
            auth.uid!!
        )
        documentReference.update("status", "Online").addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "Now User is Online",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
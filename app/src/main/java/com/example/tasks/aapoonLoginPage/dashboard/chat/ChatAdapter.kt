package com.example.tasks.aapoonLoginPage.dashboard.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.chatBox.ChatBoxActivity
import com.example.tasks.profile.Profile
import java.util.*

// Chat box user adapter
class ChatAdapter(val context: Context, private val userList: ArrayList<Profile>):
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.chat_box_layout, parent, false)
        return ChatViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.firstName
        holder.profileImage.setImageBitmap(currentUser.image?.let { base64ToBitmap(it) })

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatBoxActivity::class.java)
            intent.putExtra("name", currentUser.firstName)
            intent.putExtra("phone", currentUser.phone )
            intent.putExtra("profile", currentUser.image)
            context.startActivity(intent)

            // sets used item to first position
            val removedItem: Profile = userList.removeAt(position)
            userList.add(0, removedItem)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textName: TextView = itemView.findViewById(R.id.chat_box_name_text_view)
        val profileImage: ImageView = itemView.findViewById(R.id.chat_profile_image_view)
    }

    // converts bitmap image to normal image
    @RequiresApi(Build.VERSION_CODES.O)
    private fun base64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes: ByteArray = Base64.getDecoder().decode(b64)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }

}
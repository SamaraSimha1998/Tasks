package com.example.tasks.chatBox

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
import java.util.*

// Chat box user adapter
class ChatBoxAdapter(val context: Context, private val userList: ArrayList<ChatBoxUser>):
    RecyclerView.Adapter<ChatBoxAdapter.ChatBoxViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBoxViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.chat_box_layout, parent, false)
        return ChatBoxViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatBoxViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.name
        holder.profileImage.setImageBitmap(currentUser.profile?.let { base64ToBitmap(it) })

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatBoxActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid )
            intent.putExtra("profile", currentUser.profile)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ChatBoxViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
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
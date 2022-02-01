package com.example.tasks.chatBox

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R

// Chat box user adapter
class ChatBoxAdapter(val context: Context, private val userList: ArrayList<ChatBoxUser>):
    RecyclerView.Adapter<ChatBoxAdapter.ChatBoxViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBoxViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.chat_box_layout, parent, false)
        return ChatBoxViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatBoxViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatBoxActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ChatBoxViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textName: TextView = itemView.findViewById(R.id.chat_box_name_text_view)
    }
}
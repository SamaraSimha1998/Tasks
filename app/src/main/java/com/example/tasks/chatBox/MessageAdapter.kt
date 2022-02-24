package com.example.tasks.chatBox

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.google.firebase.auth.FirebaseAuth
import java.util.stream.IntStream.range


class MessageAdapter(val context: Context, private val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemReceive = 1
    private val itemSend = 2
    private val channelId = "com.example.tasks"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 1){
            // Inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)
        }else{
            // Inflate send
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            SendViewHolder(view)
        }
    }

    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SendViewHolder::class.java){
            // code for send viewHolder
            val viewHolder = holder as SendViewHolder
            holder.sendMessage.text = currentMessage.message
        }else{
            // code for receive viewHolder
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
            if(position == messageList.size-1){
                for(i in range(0,1)){
                    notification(currentMessage.message)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            itemSend
        }else{
            itemReceive
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sendMessage: TextView = itemView.findViewById(R.id.send_text_view)
    }

    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receiveMessage: TextView = itemView.findViewById(R.id.receive_text_view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
    private fun notification(message: String?){
        val manager = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle(FirebaseAuth.getInstance().currentUser.toString())
            .setContentText(message)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(0, manager)

        val intent = Intent(context, ChatBoxActivity::class.java)
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        manager.contentIntent
    }
}
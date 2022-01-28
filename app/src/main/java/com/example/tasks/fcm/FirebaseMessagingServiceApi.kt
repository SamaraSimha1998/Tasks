package com.example.tasks.fcm

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.tasks.MainActivity
import com.example.tasks.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingServiceApi : FirebaseMessagingService() {

//    private val TAG = "FirebaseMessagingService"

    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            try {
                val data = JSONObject(remoteMessage.data as Map<*, *>)
                val jsonMessage = data.getString("extra_information")
                Log.d(
                    TAG, """
     onMessageReceived: 
     Extra Information: $jsonMessage
     """.trimIndent()
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title //get title
            val message = remoteMessage.notification!!.body //get message
            val clickAction = remoteMessage.notification!!.clickAction //get click_action
            Log.d(
                TAG,
                "Message Notification Title: $title"
            )
            Log.d(
                TAG,
                "Message Notification Body: $message"
            )
            Log.d(
                TAG,
                "Message Notification click_action: $clickAction"
            )
            if (title != null) {
                if (message != null) {
                    if (clickAction != null) {
                        sendNotification(title, message, clickAction)
                    }
                }
            }
        }
    }

    override fun onDeletedMessages() {}

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(title: String, message: String, click_action: String) {
        val intent: Intent
        when (click_action) {

            // from message, Navigates to required activity
            "SOMEACTIVITY" -> {
                intent = Intent(this, SomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            "MAINACTIVITY" -> {
                intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            else -> {
                intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        // Uses default notification sound for message
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.aapoon_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }



    companion object {
        private const val TAG = "FirebaseMessagingServiceApi"
    }
}
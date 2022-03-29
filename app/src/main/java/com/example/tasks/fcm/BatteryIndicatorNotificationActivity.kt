package com.example.tasks.fcm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_battery_indicator_notification.*


class BatteryIndicatorNotificationActivity : AppCompatActivity() {

    private val notificationId = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_indicator_notification)

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)

        this.registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            val batteryPercentage = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL,0)

            indicator_text_view.text = batteryPercentage.toString() + "%"

            if (batteryPercentage != null) {
                alarm(batteryPercentage)
            }
        }
    }

    private fun alarm(batteryLevel: Int) {
        if (batteryLevel <= 15) {
            Toast.makeText(this, "Please charge your mobile", Toast.LENGTH_SHORT).show()

            // Gets default ringtone from mobile
            try {
                val notification: Uri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(applicationContext, notification)
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Creates notification
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Battery Level")
                .setContentText("Please charge your mobile")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }
    }
}
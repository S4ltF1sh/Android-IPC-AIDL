package com.s4ltf1sh.aidl_motherapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.s4ltf1sh.aidl_motherapp.model.MyMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AIDLExampleService : Service() {
    companion object {
        const val CHANNEL_ID = 6969
        const val NOTIFICATION_ID = 1
    }

    private val mBinder = object : IExampleInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            // TODO Auto-generated method stub
        }

        override fun getMessageFromMother(): String {
            return "Hello from Mother"
        }

        override fun sendMessageToMother(message: String?) {
            postNotification(message)
        }

        override fun sendMessageObjToMother(message: MyMessage?) {
            postNotification(message?.content, message?.time ?: -1)
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    private fun postNotification(message: String?, time: Long) {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = format.format(time)
        val str = "$message\nat $date"
        postNotification(str)
    }

    private fun postNotification(message: String?) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID.toString())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Message from child")
            .setContentText(message ?: "Message null")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notification = builder.build()

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Mother App"
            val description = "Noti to show message received from child app"
            val important = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID.toString(), name, important).apply {
                this.description = description
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
    }

}
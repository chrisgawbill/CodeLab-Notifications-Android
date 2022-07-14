package com.example.notifyme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat


class MainActivity : AppCompatActivity() {
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    private val ACTION_UPDATE_NOTIFICATION = "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION"
    private val NOTIFICATION_ID = 0

    lateinit var notifyButton: Button
    lateinit var notificationManager:NotificationManager
    lateinit var update:Button
    lateinit var cancel:Button

    var notificationReciever:NotificationReciever = NotificationReciever()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        registerReceiver(notificationReciever, IntentFilter(ACTION_UPDATE_NOTIFICATION))

        notifyButton = findViewById<Button>(R.id.notifyButton)
        notifyButton.setOnClickListener(View.OnClickListener {
            sendNotifcation()
        })
        update = findViewById<Button>(R.id.update)
        update.setOnClickListener(View.OnClickListener {
            updateNotication()
        })
        cancel = findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener(View.OnClickListener {
            cancelNotification()
        })

    }
    fun createNotificationChannel(){
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            var notificationChannel:NotificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID, "Mascot Notificaiton", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableVibration(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.description = "Notification from mascot"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    fun sendNotifcation(){
        var intent:Intent = Intent(ACTION_UPDATE_NOTIFICATION)
        var pendingIntent:PendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT)

        var notifyBuilder:NotificationCompat.Builder = getNotificationBuilder()
        notifyBuilder.addAction(R.drawable.ic_update, "Update Notification", pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())



    }
    private fun getNotificationBuilder(): NotificationCompat.Builder {
        //Intent to launch the main activity
        val notificationIntent = Intent(this, MainActivity::class.java)
        //Creates a pending intent (an intent that allows it be fired on behalf of you)
        val notificationPendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        //Creating Notification builder
        val notifyBuilder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You have been notified")
            .setContentText("Notification Text")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        setNotificationButtonState(false, true, true);
        return notifyBuilder
    }
    fun updateNotication(){
        val androidImage:Bitmap = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val notifyBuilder:NotificationCompat.Builder = getNotificationBuilder()
        notifyBuilder.setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!")
        )
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        setNotificationButtonState(false, false, true);
    }
    fun cancelNotification(){
        notificationManager.cancel(NOTIFICATION_ID)
        setNotificationButtonState(true, false, false);
    }
    fun setNotificationButtonState(
        isNotifyEnabled: Boolean?,
        isUpdateEnabled: Boolean?,
        isCancelEnabled: Boolean?
    ) {
        if (isNotifyEnabled != null) {
            notifyButton.setEnabled(isNotifyEnabled)
        }
        if (isUpdateEnabled != null) {
            notifyButton.setEnabled(isUpdateEnabled)
        }
        if (isCancelEnabled != null) {
            notifyButton.setEnabled(isCancelEnabled)
        }
    }
    override fun onDestroy() {
        unregisterReceiver(notificationReciever)
        super.onDestroy()
    }
    inner class NotificationReciever:BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            updateNotication()
        }

    }
}
package com.example.notifyme

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat


class MainActivity : AppCompatActivity() {
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    private val NOTIFICATION_ID = 0

    lateinit var notifyButton: Button
    lateinit var notificationManager:NotificationManager
    lateinit var update:Button
    lateinit var cancel:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notifyButton.findViewById<Button>(R.id.notify)
        notifyButton.setOnClickListener(View.OnClickListener {
            sendNotifcation()
        })
        update.findViewById<Button>(R.id.update)
        update.setOnClickListener(View.OnClickListener {
            updateNotication()
        })
        cancel.findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener(View.OnClickListener {
            cancelNotification()
        })

        //createNotificationChannel()
    }
    fun sendNotifcation(){
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            var notificationChannel:NotificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID, "Mascot Notification", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification from Mastcot"
            notificationManager.createNotificationChannel(notificationChannel)

            val notifyBuilder = getNotificationBuilder()
            notificationManager.notify(NOTIFICATION_ID, notifyBuilder!!.build())

            setNotificationButtonState(true, false, false);
        }
    }
    private fun getNotificationBuilder(): NotificationCompat.Builder? {
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
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val notifyBuilder = getNotificationBuilder()
        notifyBuilder!!.setStyle(
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
}
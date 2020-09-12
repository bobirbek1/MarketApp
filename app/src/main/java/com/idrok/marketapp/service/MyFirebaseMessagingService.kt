package com.idrok.marketapp.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val PENDING_INTENT_REQ_CODE = 1002

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "token:$token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "onMessageRecieved:called")
        Log.d("FCM", "Recieved message from: ${message.from}")

//        if (message.notification != null) {
//            Log.d("FCM", "data: ${message.data}")
//            val title = message.notification!!.title
//            val body = message.notification!!.body
//            val docPath = message.data["DOC_PATH"]
//
//            Log.d("FCM", "docPath: $docPath")
//
//            val intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.putExtra("DOC_PATH", docPath)
//            val pendingIntent = PendingIntent.getActivity(
//                this,
//                PENDING_INTENT_REQ_CODE,
//                intent,
//                PendingIntent.FLAG_ONE_SHOT
//            )
//
//            val notification = NotificationCompat.Builder(this, FCM_CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .build()
//            val manager = getSystemService(NotificationManager::class.java)
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                val fmChannel = NotificationChannel(
//                    FCM_CHANNEL_ID, "FCM channel", NotificationManager.IMPORTANCE_HIGH
//                )
//                manager?.createNotificationChannel(fmChannel)
//            }
//            manager?.notify(1001, notification)
//        }
    }
}


package com.idrok.marketapp

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

const val PREFERENCE_KEY = "com.example.marketapp.PREFERENCE_KEY"
const val IS_USER_REGISTRATE = "isUserRegistrate"
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
        }
            .addOnFailureListener {
            }
        FirebaseMessaging.getInstance().subscribeToTopic("MarketApp")
            .addOnCompleteListener {
            }
    }
}
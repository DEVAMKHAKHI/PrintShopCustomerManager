package com.example.printshopmanager

import android.app.Application
import com.google.firebase.FirebaseApp

class PrintShopApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase if not auto-initialized by ContentProvider
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
    }
}

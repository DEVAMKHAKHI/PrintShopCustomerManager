package com.example.printshopmanager.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 * Singleton Firebase manager providing configured instances of
 * Firebase Authentication and Cloud Firestore.
 */
object FirebaseManager {
    const val COLLECTION_CUSTOMERS = "customers"
    const val COLLECTION_USERS = "users"

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val firestore: FirebaseFirestore by lazy {
        val db = FirebaseFirestore.getInstance()
        try {
            // Enable offline persistence settings
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            db.firestoreSettings = settings
        } catch (_: Exception) {
            // Settings already set or running in test environment
        }
        db
    }

    val customersCollection: CollectionReference
        get() = firestore.collection(COLLECTION_CUSTOMERS)

    val usersCollection: CollectionReference
        get() = firestore.collection(COLLECTION_USERS)

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun currentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun currentUserEmail(): String? {
        return auth.currentUser?.email
    }
}

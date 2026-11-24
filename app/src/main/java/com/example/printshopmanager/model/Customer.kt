package com.example.printshopmanager.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Customer Data Model for PrintShop Customer Manager
 * Supports Firebase Cloud Firestore direct serialization and Map conversion.
 */
data class Customer(
    @DocumentId
    val customerId: String = "",

    @get:PropertyName("fullName")
    @set:PropertyName("fullName")
    var fullName: String = "",

    @get:PropertyName("phoneNumber")
    @set:PropertyName("phoneNumber")
    var phoneNumber: String = "",

    @get:PropertyName("dateOfBirth")
    @set:PropertyName("dateOfBirth")
    var dateOfBirth: String = "",

    @get:PropertyName("email")
    @set:PropertyName("email")
    var email: String = "",

    @get:PropertyName("address")
    @set:PropertyName("address")
    var address: String = "",

    @get:PropertyName("createdAt")
    @set:PropertyName("createdAt")
    var createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Converts Customer instance into Firestore compatible HashMap
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "fullName" to fullName,
            "phoneNumber" to phoneNumber,
            "dateOfBirth" to dateOfBirth,
            "email" to email,
            "address" to address,
            "createdAt" to createdAt
        )
    }

    /**
     * Returns 1 or 2 letter uppercase initials for customer avatar badge
     */
    val initials: String
        get() {
            val parts = fullName.trim().split("\\s+".toRegex())
            return when {
                parts.isEmpty() || parts[0].isEmpty() -> "P"
                parts.size == 1 -> parts[0].take(2).uppercase(Locale.ROOT)
                else -> "${parts[0].first()}${parts[1].first()}".uppercase(Locale.ROOT)
            }
        }

    /**
     * Formats createdAt timestamp to human readable date string
     */
    val formattedRegistrationDate: String
        get() {
            return try {
                val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                sdf.format(Date(createdAt))
            } catch (e: Exception) {
                "Recent"
            }
        }

    companion object {
        fun fromMap(id: String, map: Map<String, Any?>): Customer {
            return Customer(
                customerId = id,
                fullName = (map["fullName"] as? String).orEmpty(),
                phoneNumber = (map["phoneNumber"] as? String).orEmpty(),
                dateOfBirth = (map["dateOfBirth"] as? String).orEmpty(),
                email = (map["email"] as? String).orEmpty(),
                address = (map["address"] as? String).orEmpty(),
                createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }
    }
}

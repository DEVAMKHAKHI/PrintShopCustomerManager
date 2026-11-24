package com.example.printshopmanager.model

/**
 * Represents an authenticated staff / manager user of PrintShop
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = ""
)

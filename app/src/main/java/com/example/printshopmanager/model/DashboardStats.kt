package com.example.printshopmanager.model

/**
 * Dashboard metrics summary
 */
data class DashboardStats(
    val totalCustomers: Int = 0,
    val recentCustomers: List<Customer> = emptyList(),
    val isSeeded: Boolean = false
)

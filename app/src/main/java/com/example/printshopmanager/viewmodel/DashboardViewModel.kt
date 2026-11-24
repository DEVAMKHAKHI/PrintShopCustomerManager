package com.example.printshopmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.printshopmanager.model.Customer
import com.example.printshopmanager.model.DashboardStats
import com.example.printshopmanager.model.UiState
import com.example.printshopmanager.model.User
import com.example.printshopmanager.repository.AuthRepository
import com.example.printshopmanager.repository.CustomerRepository
import com.example.printshopmanager.repository.FirebaseAuthRepository
import com.example.printshopmanager.repository.FirestoreCustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val customerRepository: CustomerRepository = FirestoreCustomerRepository(),
    private val authRepository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.authStateFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), authRepository.getCurrentUser())

    val dashboardStats: StateFlow<DashboardStats> = customerRepository.getCustomersFlow()
        .map { customers ->
            DashboardStats(
                totalCustomers = customers.size,
                recentCustomers = customers.take(5),
                isSeeded = customers.any { it.customerId.startsWith("seed_cust_") }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardStats()
        )

    private val _seedState = MutableStateFlow<UiState<Int>>(UiState.Idle)
    val seedState: StateFlow<UiState<Int>> = _seedState.asStateFlow()

    fun seedDummyData() {
        viewModelScope.launch {
            _seedState.value = UiState.Loading
            val result = customerRepository.seedDummyData()
            _seedState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Failed to seed dummy data.") }
            )
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun resetSeedState() {
        _seedState.value = UiState.Idle
    }
}

package com.example.printshopmanager.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.printshopmanager.model.Customer
import com.example.printshopmanager.model.CustomerFormState
import com.example.printshopmanager.model.UiState
import com.example.printshopmanager.repository.CustomerRepository
import com.example.printshopmanager.repository.FirestoreCustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val repository: CustomerRepository = FirestoreCustomerRepository()
) : ViewModel() {

    // Customer List State (Real-time Firestore updates)
    val customersState: StateFlow<UiState<List<Customer>>> = repository.getCustomersFlow()
        .map<List<Customer>, UiState<List<Customer>>> { UiState.Success(it) }
        .catch { emit(UiState.Error(it.localizedMessage ?: "Unable to fetch customers")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    // Search Query State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filtered Customers based on Search Query
    val filteredCustomers: StateFlow<List<Customer>> = combine(
        customersState,
        _searchQuery
    ) { state, query ->
        val queryLower = query.trim().lowercase()
        if (state is UiState.Success) {
            if (queryLower.isEmpty()) {
                state.data
            } else {
                state.data.filter { customer ->
                    customer.fullName.lowercase().contains(queryLower) ||
                            customer.phoneNumber.lowercase().contains(queryLower) ||
                            customer.email.lowercase().contains(queryLower) ||
                            customer.address.lowercase().contains(queryLower)
                }
            }
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Customer Detail State
    private val _customerDetailState = MutableStateFlow<UiState<Customer>>(UiState.Idle)
    val customerDetailState: StateFlow<UiState<Customer>> = _customerDetailState.asStateFlow()

    // Form State (For Registration & Edit)
    private val _formState = MutableStateFlow(CustomerFormState())
    val formState: StateFlow<CustomerFormState> = _formState.asStateFlow()

    // Form Save Operation State
    private val _saveState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val saveState: StateFlow<UiState<String>> = _saveState.asStateFlow()

    // Delete Operation State
    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteState: StateFlow<UiState<Unit>> = _deleteState.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun onFullNameChanged(name: String) {
        val error = if (name.trim().isEmpty()) "Full Name is required" else null
        _formState.value = _formState.value.copy(fullName = name, fullNameError = error)
    }

    fun onPhoneNumberChanged(phone: String) {
        val trimmed = phone.trim()
        val error = when {
            trimmed.isEmpty() -> "Phone number is required"
            trimmed.length < 7 -> "Phone number is too short"
            !trimmed.matches("^[+0-9()\\s\\-]+$".toRegex()) -> "Invalid phone format"
            else -> null
        }
        _formState.value = _formState.value.copy(phoneNumber = phone, phoneError = error)
    }

    fun onDateOfBirthChanged(dob: String) {
        val error = if (dob.trim().isEmpty()) "Date of Birth is required" else null
        _formState.value = _formState.value.copy(dateOfBirth = dob, dobError = error)
    }

    fun onEmailChanged(email: String) {
        val trimmed = email.trim()
        val error = when {
            trimmed.isEmpty() -> "Email address is required"
            !Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> "Please enter a valid email address"
            else -> null
        }
        _formState.value = _formState.value.copy(email = email, emailError = error)
    }

    fun onAddressChanged(address: String) {
        val error = when {
            address.trim().isEmpty() -> "Full Address is required"
            address.trim().length < 5 -> "Address is too short (min 5 chars)"
            else -> null
        }
        _formState.value = _formState.value.copy(address = address, addressError = error)
    }

    fun validateForm(): Boolean {
        val form = _formState.value
        val nameErr = if (form.fullName.trim().isEmpty()) "Full Name is required" else null

        val phoneTrimmed = form.phoneNumber.trim()
        val phoneErr = when {
            phoneTrimmed.isEmpty() -> "Phone number is required"
            phoneTrimmed.length < 7 -> "Phone number is too short"
            !phoneTrimmed.matches("^[+0-9()\\s\\-]+$".toRegex()) -> "Invalid phone format"
            else -> null
        }

        val dobErr = if (form.dateOfBirth.trim().isEmpty()) "Date of Birth is required" else null

        val emailTrimmed = form.email.trim()
        val emailErr = when {
            emailTrimmed.isEmpty() -> "Email address is required"
            !Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches() -> "Please enter a valid email address"
            else -> null
        }

        val addrErr = when {
            form.address.trim().isEmpty() -> "Full Address is required"
            form.address.trim().length < 5 -> "Address is too short (min 5 chars)"
            else -> null
        }

        val isValid = nameErr == null && phoneErr == null && dobErr == null && emailErr == null && addrErr == null

        _formState.value = form.copy(
            fullNameError = nameErr,
            phoneError = phoneErr,
            dobError = dobErr,
            emailError = emailErr,
            addressError = addrErr,
            isFormValid = isValid
        )

        return isValid
    }

    fun saveCustomer() {
        if (!validateForm()) return

        val form = _formState.value
        val customer = Customer(
            fullName = form.fullName.trim(),
            phoneNumber = form.phoneNumber.trim(),
            dateOfBirth = form.dateOfBirth.trim(),
            email = form.email.trim(),
            address = form.address.trim(),
            createdAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            _saveState.value = UiState.Loading
            val result = repository.addCustomer(customer)
            _saveState.value = result.fold(
                onSuccess = {
                    resetForm()
                    UiState.Success(it)
                },
                onFailure = { UiState.Error(it.localizedMessage ?: "Failed to save customer") }
            )
        }
    }

    fun updateCustomer(customerId: String) {
        if (!validateForm()) return

        val form = _formState.value
        val updatedCustomer = Customer(
            customerId = customerId,
            fullName = form.fullName.trim(),
            phoneNumber = form.phoneNumber.trim(),
            dateOfBirth = form.dateOfBirth.trim(),
            email = form.email.trim(),
            address = form.address.trim()
        )

        viewModelScope.launch {
            _saveState.value = UiState.Loading
            val result = repository.updateCustomer(updatedCustomer)
            _saveState.value = result.fold(
                onSuccess = {
                    // Update current detail state if matching
                    loadCustomerDetails(customerId)
                    UiState.Success(customerId)
                },
                onFailure = { UiState.Error(it.localizedMessage ?: "Failed to update customer") }
            )
        }
    }

    fun loadCustomerDetails(customerId: String) {
        viewModelScope.launch {
            _customerDetailState.value = UiState.Loading
            val result = repository.getCustomerById(customerId)
            _customerDetailState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Failed to load customer") }
            )
        }
    }

    fun populateForEdit(customer: Customer) {
        _formState.value = CustomerFormState(
            fullName = customer.fullName,
            phoneNumber = customer.phoneNumber,
            dateOfBirth = customer.dateOfBirth,
            email = customer.email,
            address = customer.address
        )
    }

    fun deleteCustomer(customerId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            val result = repository.deleteCustomer(customerId)
            _deleteState.value = result.fold(
                onSuccess = {
                    onSuccess()
                    UiState.Success(Unit)
                },
                onFailure = { UiState.Error(it.localizedMessage ?: "Failed to delete customer") }
            )
        }
    }

    fun resetForm() {
        _formState.value = CustomerFormState()
        _saveState.value = UiState.Idle
    }

    fun resetSaveState() {
        _saveState.value = UiState.Idle
    }

    fun resetDeleteState() {
        _deleteState.value = UiState.Idle
    }
}

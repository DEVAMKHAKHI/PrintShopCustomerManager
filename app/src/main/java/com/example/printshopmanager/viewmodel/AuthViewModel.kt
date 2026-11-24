package com.example.printshopmanager.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.printshopmanager.model.AuthFormState
import com.example.printshopmanager.model.UiState
import com.example.printshopmanager.model.User
import com.example.printshopmanager.repository.AuthRepository
import com.example.printshopmanager.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.authStateFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), authRepository.getCurrentUser())

    private val _loginState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val loginState: StateFlow<UiState<User>> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val registerState: StateFlow<UiState<User>> = _registerState.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val resetPasswordState: StateFlow<UiState<Unit>> = _resetPasswordState.asStateFlow()

    private val _formState = MutableStateFlow(AuthFormState())
    val formState: StateFlow<AuthFormState> = _formState.asStateFlow()

    fun onEmailChange(email: String) {
        val error = validateEmail(email)
        _formState.value = _formState.value.copy(
            email = email,
            emailError = error
        )
    }

    fun onPasswordChange(password: String) {
        val error = validatePassword(password)
        _formState.value = _formState.value.copy(
            password = password,
            passwordError = error
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        val error = if (confirmPassword != _formState.value.password) {
            "Passwords do not match"
        } else null
        _formState.value = _formState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = error
        )
    }

    private fun validateEmail(email: String): String? {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> "Email address is required"
            !Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> "Please enter a valid email address"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    fun login(email: String, password: String) {
        val emailErr = validateEmail(email)
        val passErr = validatePassword(password)

        if (emailErr != null || passErr != null) {
            _formState.value = _formState.value.copy(
                emailError = emailErr,
                passwordError = passErr
            )
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val result = authRepository.login(email, password)
            _loginState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Failed to log in. Please check credentials.") }
            )
        }
    }

    fun register(email: String, password: String, displayName: String = "") {
        val emailErr = validateEmail(email)
        val passErr = validatePassword(password)
        val confirmErr = if (_formState.value.confirmPassword != password) "Passwords do not match" else null

        if (emailErr != null || passErr != null || confirmErr != null) {
            _formState.value = _formState.value.copy(
                emailError = emailErr,
                passwordError = passErr,
                confirmPasswordError = confirmErr
            )
            return
        }

        viewModelScope.launch {
            _registerState.value = UiState.Loading
            val result = authRepository.register(email, password, displayName)
            _registerState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Registration failed.") }
            )
        }
    }

    fun sendPasswordReset(email: String) {
        val emailErr = validateEmail(email)
        if (emailErr != null) {
            _formState.value = _formState.value.copy(emailError = emailErr)
            return
        }

        viewModelScope.launch {
            _resetPasswordState.value = UiState.Loading
            val result = authRepository.sendPasswordReset(email)
            _resetPasswordState.value = result.fold(
                onSuccess = { UiState.Success(Unit) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Could not send password reset email.") }
            )
        }
    }

    fun logout() {
        authRepository.logout()
        resetStates()
    }

    fun resetStates() {
        _loginState.value = UiState.Idle
        _registerState.value = UiState.Idle
        _resetPasswordState.value = UiState.Idle
        _formState.value = AuthFormState()
    }
}

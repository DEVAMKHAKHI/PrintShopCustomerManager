package com.example.printshopmanager.model

/**
 * Validation result holder for input fields
 */
data class FieldValidation(
    val isValid: Boolean = true,
    val errorMessage: String? = null
)

/**
 * Customer Form validation state
 */
data class CustomerFormState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val email: String = "",
    val address: String = "",
    val fullNameError: String? = null,
    val phoneError: String? = null,
    val dobError: String? = null,
    val emailError: String? = null,
    val addressError: String? = null,
    val isFormValid: Boolean = false
) {
    fun hasErrors(): Boolean {
        return fullNameError != null || phoneError != null || dobError != null ||
                emailError != null || addressError != null
    }
}

/**
 * Auth Form validation state
 */
data class AuthFormState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isFormValid: Boolean = false
)

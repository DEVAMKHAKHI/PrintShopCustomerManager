package com.example.printshopmanager.screens.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.printshopmanager.components.CustomTextField
import com.example.printshopmanager.components.DatePickerModal
import com.example.printshopmanager.components.ErrorBanner
import com.example.printshopmanager.components.PrintShopTopAppBar
import com.example.printshopmanager.model.UiState
import com.example.printshopmanager.viewmodel.CustomerViewModel

@Composable
fun AddCustomerScreen(
    customerViewModel: CustomerViewModel,
    onNavigateBack: () -> Unit,
    onCustomerSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formState by customerViewModel.formState.collectAsState()
    val saveState by customerViewModel.saveState.collectAsState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by remember { mutableStateOf(false) }

    val isLoading = saveState is UiState.Loading

    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) {
            customerViewModel.resetSaveState()
            onCustomerSaved()
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { selectedDate ->
                customerViewModel.onDateOfBirthChanged(selectedDate)
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            PrintShopTopAppBar(
                title = "Register New Customer",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Client Registration Form",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Add contact, profile and billing address details for new print shop client.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 18.dp)
            )

            if (saveState is UiState.Error) {
                ErrorBanner(
                    message = (saveState as UiState.Error).message,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Full Name
                    CustomTextField(
                        value = formState.fullName,
                        onValueChange = { customerViewModel.onFullNameChanged(it) },
                        label = "Full Name *",
                        placeholder = "e.g. Apex Graphic Design Studio",
                        leadingIcon = Icons.Default.Person,
                        errorMessage = formState.fullNameError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Phone Number
                    CustomTextField(
                        value = formState.phoneNumber,
                        onValueChange = { customerViewModel.onPhoneNumberChanged(it) },
                        label = "Phone Number *",
                        placeholder = "e.g. 555-0101",
                        leadingIcon = Icons.Default.Phone,
                        errorMessage = formState.phoneError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Date of Birth (with DatePicker modal)
                    CustomTextField(
                        value = formState.dateOfBirth,
                        onValueChange = {},
                        label = "Date of Birth *",
                        placeholder = "YYYY-MM-DD",
                        readOnly = true,
                        leadingIcon = Icons.Default.CalendarMonth,
                        errorMessage = formState.dobError,
                        modifier = Modifier.clickable { showDatePicker = true },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Pick Date",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { showDatePicker = true }
                            )
                        }
                    )

                    // Email Address
                    CustomTextField(
                        value = formState.email,
                        onValueChange = { customerViewModel.onEmailChanged(it) },
                        label = "Email Address *",
                        placeholder = "e.g. info@apexdesigns.example",
                        leadingIcon = Icons.Default.Email,
                        errorMessage = formState.emailError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Full Address
                    CustomTextField(
                        value = formState.address,
                        onValueChange = { customerViewModel.onAddressChanged(it) },
                        label = "Full Address *",
                        placeholder = "e.g. 101 Creative Blvd, Suite 400, Chicago, IL",
                        leadingIcon = Icons.Default.Home,
                        errorMessage = formState.addressError,
                        singleLine = false,
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                customerViewModel.saveCustomer()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action Buttons (Save Customer & Cancel / Reset)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                customerViewModel.resetForm()
                                onNavigateBack()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                customerViewModel.saveCustomer()
                            },
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(50.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    text = "Save Customer",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

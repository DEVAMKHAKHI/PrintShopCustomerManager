package com.example.printshopmanager.screens.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.printshopmanager.components.CustomerCard
import com.example.printshopmanager.components.EmptyStateView
import com.example.printshopmanager.components.ErrorBanner
import com.example.printshopmanager.components.LoadingIndicator
import com.example.printshopmanager.components.PrintShopSearchBar
import com.example.printshopmanager.components.PrintShopTopAppBar
import com.example.printshopmanager.model.UiState
import com.example.printshopmanager.viewmodel.CustomerViewModel

@Composable
fun CustomerListScreen(
    customerViewModel: CustomerViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddCustomer: () -> Unit,
    onNavigateToCustomerDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val customersState by customerViewModel.customersState.collectAsState()
    val filteredCustomers by customerViewModel.filteredCustomers.collectAsState()
    val searchQuery by customerViewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            PrintShopTopAppBar(
                title = "Customer Directory",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddCustomer,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Customer")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input Field
            PrintShopSearchBar(
                query = searchQuery,
                onQueryChange = { customerViewModel.onSearchQueryChanged(it) },
                onClear = { customerViewModel.clearSearch() }
            )

            // Results count badge
            if (customersState is UiState.Success) {
                val total = (customersState as UiState.Success).data.size
                val count = filteredCustomers.size

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isEmpty()) {
                            "Showing all $total customers"
                        } else {
                            "Found $count of $total customers"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Screen Content based on UiState
            when (val state = customersState) {
                is UiState.Loading -> {
                    LoadingIndicator(message = "Retrieving customer directory from Firestore...")
                }
                is UiState.Error -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ErrorBanner(message = state.message)
                    }
                }
                is UiState.Success -> {
                    val allCustomers = state.data
                    if (allCustomers.isEmpty()) {
                        EmptyStateView(
                            title = "No Customers Found",
                            subtitle = "No customer records currently exist in Cloud Firestore. Add a new customer to get started.",
                            actionButtonText = "+ Add First Customer",
                            onActionClick = onNavigateToAddCustomer
                        )
                    } else if (filteredCustomers.isEmpty() && searchQuery.isNotEmpty()) {
                        EmptyStateView(
                            title = "No Matches Found",
                            subtitle = "No customer records match \"$searchQuery\". Try checking the spelling or search by phone/email.",
                            icon = Icons.Default.SearchOff,
                            actionButtonText = "Clear Search",
                            onActionClick = { customerViewModel.clearSearch() }
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = filteredCustomers,
                                key = { it.customerId }
                            ) { customer ->
                                CustomerCard(
                                    customer = customer,
                                    onClick = { onNavigateToCustomerDetail(customer.customerId) }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

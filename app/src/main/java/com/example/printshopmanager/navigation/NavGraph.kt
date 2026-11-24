package com.example.printshopmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.printshopmanager.screens.auth.ForgotPasswordScreen
import com.example.printshopmanager.screens.auth.LoginScreen
import com.example.printshopmanager.screens.auth.SignUpScreen
import com.example.printshopmanager.screens.customer.AddCustomerScreen
import com.example.printshopmanager.screens.customer.CustomerDetailScreen
import com.example.printshopmanager.screens.customer.CustomerListScreen
import com.example.printshopmanager.screens.customer.EditCustomerScreen
import com.example.printshopmanager.screens.dashboard.DashboardScreen
import com.example.printshopmanager.viewmodel.AuthViewModel
import com.example.printshopmanager.viewmodel.CustomerViewModel
import com.example.printshopmanager.viewmodel.DashboardViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    customerViewModel: CustomerViewModel = viewModel(),
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Auth: Login
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    authViewModel.resetStates()
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateToForgotPassword = {
                    authViewModel.resetStates()
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Auth: Sign Up
        composable(Screen.SignUp.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateBack = {
                    authViewModel.resetStates()
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Auth: Forgot Password
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                authViewModel = authViewModel,
                onNavigateBack = {
                    authViewModel.resetStates()
                    navController.popBackStack()
                }
            )
        }

        // Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                dashboardViewModel = dashboardViewModel,
                onNavigateToAddCustomer = {
                    customerViewModel.resetForm()
                    navController.navigate(Screen.AddCustomer.route)
                },
                onNavigateToCustomerList = {
                    customerViewModel.clearSearch()
                    navController.navigate(Screen.CustomerList.route)
                },
                onNavigateToCustomerDetail = { customerId ->
                    navController.navigate(Screen.CustomerDetail.createRoute(customerId))
                },
                onLogout = {
                    dashboardViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        // Customer List
        composable(Screen.CustomerList.route) {
            CustomerListScreen(
                customerViewModel = customerViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddCustomer = {
                    customerViewModel.resetForm()
                    navController.navigate(Screen.AddCustomer.route)
                },
                onNavigateToCustomerDetail = { customerId ->
                    navController.navigate(Screen.CustomerDetail.createRoute(customerId))
                }
            )
        }

        // Add Customer Form
        composable(Screen.AddCustomer.route) {
            AddCustomerScreen(
                customerViewModel = customerViewModel,
                onNavigateBack = { navController.popBackStack() },
                onCustomerSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Customer Detail Screen
        composable(
            route = Screen.CustomerDetail.route,
            arguments = listOf(
                navArgument("customerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            CustomerDetailScreen(
                customerId = customerId,
                customerViewModel = customerViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EditCustomer.createRoute(id))
                },
                onCustomerDeleted = {
                    navController.popBackStack()
                }
            )
        }

        // Edit Customer Screen
        composable(
            route = Screen.EditCustomer.route,
            arguments = listOf(
                navArgument("customerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            EditCustomerScreen(
                customerId = customerId,
                customerViewModel = customerViewModel,
                onNavigateBack = { navController.popBackStack() },
                onCustomerUpdated = {
                    navController.popBackStack()
                }
            )
        }
    }
}

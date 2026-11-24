package com.example.printshopmanager.navigation

/**
 * Navigation routes for PrintShop Customer Manager
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object ForgotPassword : Screen("forgot_password")
    data object Dashboard : Screen("dashboard")
    data object CustomerList : Screen("customer_list")
    data object AddCustomer : Screen("add_customer")

    data object CustomerDetail : Screen("customer_detail/{customerId}") {
        fun createRoute(customerId: String): String = "customer_detail/$customerId"
    }

    data object EditCustomer : Screen("edit_customer/{customerId}") {
        fun createRoute(customerId: String): String = "edit_customer/$customerId"
    }
}

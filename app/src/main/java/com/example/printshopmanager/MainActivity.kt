package com.example.printshopmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.printshopmanager.navigation.AppNavGraph
import com.example.printshopmanager.navigation.Screen
import com.example.printshopmanager.ui.theme.PrintShopTheme
import com.example.printshopmanager.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PrintShopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val currentUser by authViewModel.currentUser.collectAsState()

                    // If user is already logged in, navigate directly to Dashboard, else Login
                    val startDestination = if (currentUser != null) {
                        Screen.Dashboard.route
                    } else {
                        Screen.Login.route
                    }

                    AppNavGraph(
                        navController = navController,
                        startDestination = startDestination,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

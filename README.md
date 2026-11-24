# PrintShop Customer Manager - Native Android Application

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![UI](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2B%20Material%203-blue.svg)](https://developer.android.com/jetpack/compose)
[![Backend](https://img.shields.io/badge/Backend-Firebase%20Auth%20%2B%20Firestore-orange.svg)](https://firebase.google.com)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-brightgreen.svg)](https://developer.android.com/topic/architecture)

A complete, production-grade native Android application developed for printing businesses to manage client accounts, corporate print orders, customer contact details, and billing relationships. Built with modern Android architecture using **Kotlin**, **Jetpack Compose**, **Material 3**, and **Firebase (Authentication & Cloud Firestore)**.

---

## 🌟 Key Application Features

1. **Authentication & Staff Access Control (Steps 4 & 6)**:
   - Email and Password registration and sign-in for printing shop staff.
   - Password reset workflow with email delivery.
   - Real-time input validation (RFC-compliant email check, minimum 6-character password).
   - Password visibility toggling with eye icon.
   - Persistent authentication state: authenticated staff automatically navigate to the Dashboard.

2. **Operations Dashboard (Step 7)**:
   - Modern Material 3 dashboard layout with personalized staff greeting.
   - Real-time **Total Customers** metric counter directly observing Firestore.
   - Quick navigation action cards for **Add New Customer** and **View Customers**.
   - Built-in one-tap **Seed 10 Sample Customers** button with duplicate detection.
   - Preview list of recently registered clients.
   - Secure Sign-Out dialog.

3. **Customer Registration & Input Validation (Step 8 & 10)**:
   - Comprehensive registration form containing:
     - Full Name (required, min 2 chars)
     - Phone Number (required, phone format validation)
     - Date of Birth (interactive Material 3 `DatePickerDialog`)
     - Email Address (RFC email validation)
     - Full Address (required, min 5 chars)
   - Real-time error messages beneath fields.
   - Automatic generation of Firestore document IDs.
   - Loading indicator and graceful error handling.

4. **Firestore Data Model & Cloud Storage (Step 9 & 10)**:
   - Strongly-typed `Customer` model supporting direct Firestore serialization (`@DocumentId`, `@PropertyName`).
   - Clean separation of business logic via `CustomerRepository`.
   - Offline persistence enabled via `FirebaseFirestoreSettings`.

5. **Realistic Dummy Data Seeder (Step 11)**:
   - 10 realistic printing shop customer records (Apex Design Studio, Metro University, Sweet Bites Bakery, etc.).
   - Deterministic IDs (`seed_cust_001` .. `seed_cust_010`) prevent duplicate data.
   - Two convenient ways to seed:
     - **In-App**: Single tap on Dashboard button.
     - **CLI**: Python script `Project_Report_Materials/seed_firestore.py`.

6. **Customer Directory & Real-Time Search (Steps 12 & 14)**:
   - Real-time reactive customer list powered by Firestore snapshot listeners.
   - Multi-criteria real-time search filtering across **Customer Name**, **Phone Number**, and **Email Address**.
   - Empty directory state and "No search matches" state with one-click clear button.

7. **Customer Details & Quick Actions (Step 13)**:
   - Complete record inspection (Name, Phone, DOB, Email, Address, Registration Date).
   - Dynamic 2-letter customer initials avatar.
   - One-tap Android system intents:
     - **Call**: Launches phone dialer with customer's phone number (`Intent.ACTION_DIAL`).
     - **Email**: Opens default email client addressed to customer (`Intent.ACTION_SENDTO`).

8. **Update & Secure Delete (Step 15)**:
   - Customer editing pre-populates existing data and updates records in Firestore.
   - Customer deletion protected by an explicit **Material 3 Confirmation Alert Dialog** to prevent accidental loss.

9. **Robust Navigation & Architecture (Steps 2 & 16)**:
   - Clean **MVVM (Model-View-ViewModel)** separation of concerns.
   - Built entirely on **Jetpack Navigation Compose** (`NavHost`, `composable`, argument passing).

10. **Firebase Security Rules (Step 17)**:
    - `firestore.rules` guarantees only authenticated staff can read and mutate customer records.
    - Field validations ensure no corrupted data enters Firestore.

---

## 🏗️ Project Architecture & Package Structure

```
com.example.printshopmanager/
│
├── PrintShopApp.kt              # Application subclass (Firebase init)
├── MainActivity.kt              # Single Activity host with Edge-to-Edge Jetpack Compose
│
├── model/                       # Data entities & UI state wrappers
│   ├── Customer.kt              # Customer data class (Firestore annotations & helpers)
│   ├── User.kt                  # Staff user model
│   ├── UiState.kt               # Sealed interface: Idle, Loading, Success, Error
│   ├── DashboardStats.kt        # Metric aggregates data class
│   └── ValidationResult.kt      # Form validation state holders
│
├── firebase/                    # Firebase helpers and configuration
│   ├── FirebaseManager.kt       # Singleton wrapper for FirebaseAuth & FirebaseFirestore
│   └── DummyDataSeeder.kt       # Idempotent 10-customer sample database seeder
│
├── repository/                  # Repository Layer (Data abstraction)
│   ├── AuthRepository.kt        # Interface + FirebaseAuthRepository
│   └── CustomerRepository.kt    # Interface + FirestoreCustomerRepository
│
├── viewmodel/                   # ViewModel Layer (Business logic & StateFlow)
│   ├── AuthViewModel.kt         # Sign-in, sign-up, reset password, validation
│   ├── CustomerViewModel.kt     # List, search, form validation, save, edit, delete
│   └── DashboardViewModel.kt    # Metrics, sample seeder, logout
│
├── navigation/                  # Jetpack Compose Navigation
│   ├── Screen.kt                # Type-safe navigation routes
│   └── NavGraph.kt              # NavHost configuration & argument routes
│
├── ui/theme/                    # Material 3 Printing Shop Brand Styling
│   ├── Color.kt                 # CMYK & Deep Navy printing palette
│   ├── Type.kt                  # Typography scale
│   └── Theme.kt                 # Material 3 light/dark theme definition
│
├── components/                  # Reusable UI widgets
│   ├── PrintShopTopAppBar.kt    # Standard top app bar
│   ├── CustomTextField.kt       # Styled text field with icons & validations
│   ├── CustomerCard.kt          # Customer directory card
│   ├── StatCard.kt              # Dashboard metric card
│   ├── PrintShopSearchBar.kt    # Search input with clear button
│   ├── DatePickerModal.kt       # Material 3 DatePickerDialog
│   ├── DeleteConfirmationDialog.kt # Safe deletion dialog
│   ├── LoadingIndicator.kt      # Progress indicator
│   ├── EmptyStateView.kt        # Empty placeholder state
│   └── ErrorBanner.kt           # Warning & error alert banner
│
└── screens/                     # Jetpack Compose feature screens
    ├── auth/
    │   ├── LoginScreen.kt
    │   ├── SignUpScreen.kt
    │   └── ForgotPasswordScreen.kt
    ├── dashboard/
    │   └── DashboardScreen.kt
    └── customer/
        ├── CustomerListScreen.kt
        ├── AddCustomerScreen.kt
        ├── CustomerDetailScreen.kt
        └── EditCustomerScreen.kt
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio Hedgehog (2023.1.1)** or newer.
- **JDK 17 or 21** (bundled automatically in Android Studio).
- Android SDK with API 34 installed.

### Opening and Running the Project
1. Clone or copy `PrintShopCustomerManager` to your development machine.
2. Open Android Studio, click **File > Open**, and select the project folder.
3. Allow Gradle to sync dependencies automatically.
4. Select an Android Virtual Device (AVD) running API 24 or newer.
5. Click the green **Run** button (or press `Shift + F10`).

---

## 🔒 Firebase Configuration & Security Rules

The application includes a pre-configured `google-services.json` file for immediate compilation. To connect your personal Firebase Console project:

1. Follow the step-by-step guide in [FIREBASE_SETUP_GUIDE.md](Project_Report_Materials/FIREBASE_SETUP_GUIDE.md).
2. Deploy the provided `firestore.rules` to secure your Firestore database against unauthorized access.

---

## 🧪 Testing

Unit tests for data models, validation, and in-memory repository operations are located in `app/src/test/java/com/example/printshopmanager/`:
- `CustomerModelTest.kt`: Unit tests for initials generation, data mapping, and date formatting.
- `FakeCustomerRepositoryTest.kt`: Tests adding, updating, searching, and deleting customers in isolation.

To run tests via CLI:
```bash
.\gradlew.bat test
```

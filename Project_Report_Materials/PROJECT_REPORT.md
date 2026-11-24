# PROJECT REPORT: PRINTSHOP CUSTOMER MANAGER
## A Cloud-Integrated Native Android Application for Printing Industry Client Management

---

### 1. COVER PAGE
**Project Title**: PrintShop Customer Manager  
**Subtitle**: Native Android Application with Jetpack Compose & Firebase Cloud Firestore  
**Domain**: Mobile Application Development / Enterprise Client Relationship Management (CRM)  
**Target Platform**: Android (API Level 24+ / Android 7.0 Nougat to Android 14)  
**Development Framework**: Android Studio, Kotlin, Jetpack Compose, Material 3, Firebase  
**Academic Year**: 2026-2027  
**Submission**: Project Report for Academic Review  

---

### 2. CERTIFICATE PLACEHOLDER
```
========================================================================================
                               CERTIFICATE OF COMPLETION
========================================================================================

This is to certify that the project entitled "PRINTSHOP CUSTOMER MANAGER" is a bonafide
work carried out by the student in partial fulfillment of the requirements for the
award of the Degree in Computer Science / Information Technology Engineering.

Project Supervisor: ____________________________        Date: ________________________
Head of Department: ____________________________        Seal: ________________________

External Examiner:  ____________________________
========================================================================================
```

---

### 3. ACKNOWLEDGEMENT
I would like to express my sincere gratitude and deep appreciation to my project guide, professors, and department faculty for their invaluable guidance, constant encouragement, and constructive criticism throughout the design, implementation, and evaluation of the **PrintShop Customer Manager** Android application.

I am also thankful to the open-source Android and Google Firebase developer communities for providing comprehensive documentation, robust SDK libraries, and architectural guidance that facilitated the realization of this application. Lastly, I extend my heartfelt appreciation to my family and colleagues for their continuous support during the completion of this academic project.

---

### 4. ABSTRACT
In commercial print shop enterprises, managing customer relations, tracking individual and corporate client details, handling custom printing specifications, and archiving contact data often relies on fragmented paper slips, basic spreadsheets, or antiquated desktop databases. These legacy techniques result in delayed communications, duplicate contact records, accidental data loss, and zero real-time mobility for floor staff and account managers.

This project, **PrintShop Customer Manager**, addresses these operational challenges by introducing a secure, responsive, and reactive native Android application. Constructed using Kotlin, Google Jetpack Compose, Material 3, and Firebase (Authentication and Cloud Firestore), the system adopts the standard Model-View-ViewModel (MVVM) architecture. Key features include role-based staff authentication, an operational metrics dashboard, a multi-field customer registration engine with interactive date pickers, real-time live-synced customer directory, full-text multi-parameter search (Name, Phone, Email), native system calling and emailing intent integrations, and safe record deletion with confirmation safeguards. Additionally, Cloud Firestore Security Rules enforce strict data governance. The application was rigorously tested across multiple virtual device profiles, verifying responsiveness, data consistency, and offline persistence.

---

### 5. INTRODUCTION
The commercial printing industry caters to a diverse clientele ranging from individual consumers and local businesses to large educational institutions, marketing agencies, and corporate enterprises. A standard printing facility handles varied orders including architectural blueprints, wedding invitations, corporate stationery, promotional banners, and high-volume offset periodicals.

Efficiently managing customer accounts is fundamental to retaining clients and accelerating project turnaround times. Account managers must promptly look up client contact information, verify delivery addresses, review client history, and coordinate proofing approvals. **PrintShop Customer Manager** delivers a specialized mobile CRM solution engineered for printing shop employees. By combining modern declarative UI design (Jetpack Compose) with real-time cloud data synchronization (Firebase Firestore), the system empowers printing shop managers to capture and manage client records anytime, anywhere on the production floor.

---

### 6. PROBLEM STATEMENT
Traditional printing shops encounter significant operational bottlenecks:
1. **Scattered and Inconsistent Data**: Client contact numbers, tax billing addresses, and job requests are frequently noted on physical paper job jackets, whiteboards, or unorganized desktop spreadsheets.
2. **Lack of Mobility**: Staff members operating digital presses or inspecting cutting tables cannot access centralized customer data without returning to a front-desk computer.
3. **Inefficient Search and Lookup**: Finding a recurring client's details during an urgent reprint requires scanning through hundreds of manual entries or multiple spreadsheet tabs.
4. **Data Redundancy and Human Error**: Multiple staff members frequently enter duplicate records for the same enterprise or miss required contact numbers.
5. **Security and Access Control Vulnerabilities**: Unprotected spreadsheets expose sensitive commercial client contacts and pricing arrangements to unauthorized access.

---

### 7. OBJECTIVES
The primary objectives of this project are:
- To design and implement a native Android application using **Kotlin** and **Jetpack Compose** following Material 3 guidelines.
- To establish an **MVVM architecture** enforcing separation between UI elements, business logic, and backend data services.
- To implement secure **Firebase Authentication** providing email/password registration, login, and password reset functionalities.
- To integrate **Firebase Cloud Firestore** for real-time, bidirectional client data synchronization with offline persistence.
- To engineer a customer registration form equipped with strict field validations, regular expression checks, and interactive date picker dialogs.
- To provide full-text, multi-field search enabling instant filtering by customer name, phone number, and email.
- To integrate native Android communication intents allowing staff to initiate phone calls or send emails directly from customer cards.
- To secure cloud data access utilizing production-grade Firestore Security Rules that prohibit unauthenticated or malformed operations.
- To supply an idempotent, duplicate-safe seeding mechanism populating at least 10 realistic sample print shop customer profiles.

---

### 8. SCOPE OF THE PROJECT
The scope encompasses:
- **Target Audience**: Printing shop store managers, customer service representatives, prepress technicians, and print floor supervisors.
- **Client Profile Scope**: Commercial printing clients, design studios, schools/universities, local retailers, legal firms, and corporate event planners.
- **Functional Scope**:
  - Staff onboarding, authentication, and session persistence.
  - Operations dashboard displaying real-time customer counts, quick actions, and recent activity.
  - Customer directory with real-time updates and search filtering.
  - New customer registration with client-side form validation.
  - Detailed customer record viewing with phone dialer and email launcher intents.
  - In-place customer editing and deletion with protective confirmation dialogs.
  - In-app and script-driven database seeding with duplicate detection.
- **Future Exclusions from Current Scope**: Direct credit card payment processing, native PDF invoice rendering, and live barcode scanning (slated for future versions).

---

### 9. EXISTING SYSTEM
In many small-to-medium printing shops, customer record management relies on manual or semi-manual legacy methods:
- **Paper-Based Record Books**: Physical ledgers kept at reception desks where client names, orders, and contact details are transcribed by hand.
- **Shared Desktop Spreadsheets (e.g. MS Excel)**: Files stored on a local computer, prone to version divergence, accidental file deletion, and file locking when accessed by multiple terminals.
- **Generic Phone Contacts**: Storing client phone numbers on an employee's personal device, leading to fragmented communications when staff members take leave.

#### Drawbacks of Existing System:
- **No Concurrent Synchronization**: Simultaneous updates result in data overwrites and conflicting copies.
- **Search Latency**: Locating an account requires manual scrolling or rudimentary keyword searches.
- **High Risk of Loss**: Physical books can be misplaced or damaged; desktop hard drives can suffer hardware failure without automated backups.
- **Zero Validation**: Incomplete phone numbers, invalid emails, or blank addresses are easily submitted without detection.

---

### 10. PROPOSED SYSTEM
The proposed **PrintShop Customer Manager** mobile solution replaces manual workflows with a native, reactive Android architecture:
- **Decoupled MVVM Architecture**: Separates UI composables from state management (`StateFlow`) and repository interfaces (`CustomerRepository`).
- **Reactive UI with Jetpack Compose**: Employs declarative components, instant state recomposition, and responsive layouts tailored for printing shop staff.
- **Instant Cloud Persistence**: Customer records entered on the production floor immediately synchronize to Google Cloud Firestore, available instantly across all authenticated devices.
- **Offline Reliability**: Firestore local caching ensures that staff can view and search existing customer profiles even during temporary network interruptions.
- **Safe Operations**: Destructive actions (deletions) require explicit secondary confirmation, and deterministic IDs prevent duplicate sample data generation.

---

### 11. SOFTWARE AND HARDWARE REQUIREMENTS

#### Software Requirements:
| Item | Specification |
|---|---|
| Operating System | Windows 10/11, macOS Ventura/Sonoma, or Linux |
| Development IDE | Android Studio Hedgehog (2023.1.1) or newer |
| Programming Language | Kotlin (version 1.9.22) |
| UI Toolkit | Jetpack Compose (BOM 2024.04.01), Material 3 |
| Compile SDK | Android SDK API Level 34 |
| Minimum SDK | Android SDK API Level 24 (Android 7.0 Nougat) |
| Target SDK | Android SDK API Level 34 (Android 14) |
| Build Tool | Gradle 8.4 (via Gradle Wrapper) |
| Cloud Backend | Google Firebase (Auth BOM 33.1.0, Cloud Firestore) |

#### Hardware Requirements:
- **Development Workstation**:
  - CPU: 64-bit Intel Core i5/i7 or AMD Ryzen 5/7 (or Apple Silicon M-series).
  - RAM: 8 GB minimum (16 GB recommended).
  - Disk Space: Minimum 10 GB free space for Android Studio, SDKs, and emulators.
- **Target Android Device / Emulator**:
  - Architecture: x86_64 or ARM64.
  - RAM: 2 GB minimum.
  - Screen Resolution: 720x1280 (HD) to 1440x3120 (Quad HD+).

---

### 12. TECHNOLOGIES USED
1. **Kotlin**: Modern, statically-typed, null-safe language officially recommended by Google for Android development.
2. **Jetpack Compose**: Modern declarative UI toolkit eliminating XML layout files, reducing boilerplate, and simplifying UI state synchronization.
3. **Material 3 (Material You)**: Google's design system providing elevation, color roles (Navy primary, Cyan secondary, Amber tertiary), and rounded card surfaces.
4. **Navigation Compose**: Type-safe navigation library handling back stack, transitions, and argument passing (`customerId`).
5. **Kotlin Coroutines & Flow**: Asynchronous programming framework delivering non-blocking concurrency, StateFlow bindings, and real-time streams.
6. **Firebase Authentication**: Managed identity platform providing email/password registration, secure credential verification, and token management.
7. **Firebase Cloud Firestore**: Scalable NoSQL document database with real-time listeners and multi-region data replication.
8. **JUnit 4**: Unit testing framework validating data models, form validators, and repository logic.

---

### 13. SYSTEM ARCHITECTURE
The application strictly implements the **Model-View-ViewModel (MVVM)** architectural pattern recommended by Google Android Architecture Guidelines:

```
+-------------------------------------------------------------------------------+
|                             VIEW / UI LAYER                                   |
|   (Jetpack Compose Screens: Login, Dashboard, CustomerList, Details, Edit)     |
+---------------------------------------+---------------------------------------+
                                        | Observes StateFlow / Dispatches Events
                                        v
+-------------------------------------------------------------------------------+
|                            VIEWMODEL LAYER                                    |
|             (AuthViewModel, CustomerViewModel, DashboardViewModel)             |
|              - Form Validation, State Holders, Coroutine Scopes               |
+---------------------------------------+---------------------------------------+
                                        | Interacts via clean Interfaces
                                        v
+-------------------------------------------------------------------------------+
|                           REPOSITORY LAYER                                    |
|                 (AuthRepository, CustomerRepository Interfaces)               |
|            - Maps raw data, handles errors, provides single source of truth   |
+---------------------------------------+---------------------------------------+
                                        | Network / Cache Queries
                                        v
+-------------------------------------------------------------------------------+
|                           DATA SOURCE LAYER                                   |
|   Firebase Authentication  <---------->  Cloud Firestore (customers collection) |
+-------------------------------------------------------------------------------+
```

---

### 14. APPLICATION FLOWCHART
```
[Application Launch]
        |
        v
[Is User Authenticated?]
      /        \
    (No)       (Yes)
    /            \
   v              v
[Login Screen]   [Operations Dashboard]
   |                     |
   +--> [Sign Up]        +--> [Total Customers Card] ---------> [Customer Directory]
   |                     |                                              |
   +--> [Forgot Pass]    +--> [Add Customer Card]                       +--> [Search Filter]
                         |          |                                   |
                         |          v                                   +--> [Select Customer]
                         |   [Add Customer Form]                                |
                         |          | (Validate & Save)                         v
                         |          v                                   [Customer Details]
                         |   [Firestore Upload]                                 |
                         |                                      +---------------+---------------+
                         +--> [Seed 10 Sample Data]             |                               |
                         |                                      v                               v
                         +--> [Sign Out] -------------> [Edit Customer Form]         [Delete Record]
                                                                |                               |
                                                                v                               v
                                                         [Save to Firestore]        [Confirm in Dialog]
                                                                                                |
                                                                                                v
                                                                                        [Delete in Firestore]
```

---

### 15. USE CASE DIAGRAM
```
                       PRINTSHOP CUSTOMER MANAGER SYSTEM
       +---------------------------------------------------------------+
       |                                                               |
       |   (( Staff Registration ))                                    |
       |             ^                                                 |
       |             |                                                 |
       |   (( Staff Login / Sign Out ))                                |
       |             ^                                                 |
       |             |                                                 |
(Staff)|---(( View Operations Dashboard Metrics ))                     |
 Member|             |                                                 |
       |---(( Register New Customer (Validate Fields) ))               |
       |             |                                                 |
       |---(( Seed Realistic Sample Customer Data ))                   |
       |             |                                                 |
       |---(( Search Customer by Name, Phone, Email ))                 |
       |             |                                                 |
       |---(( View Customer Profile (Call / Email Intents) ))          |
       |             |                                                 |
       |---(( Edit Customer Information in Firestore ))                |
       |             |                                                 |
       |---(( Delete Customer Record with Confirmation ))              |
       |                                                               |
       +---------------------------------------------------------------+
```

---

### 16. DATABASE DESIGN
The application utilizes Google Cloud Firestore, a NoSQL document database. Data is organized into collections containing JSON-like documents.

#### Collection 1: `customers`
- **Document ID**: Unique String (e.g. `seed_cust_001` or auto-generated Firestore hash `7jK9mPQ...`)
- **Document Schema**:
  | Field | Type | Description | Required | Example |
  |---|---|---|---|---|
  | `customerId` | String | Unique document ID | Yes | `"seed_cust_001"` |
  | `fullName` | String | Customer or Business Name | Yes | `"Apex Graphic Design Studio"` |
  | `phoneNumber` | String | Customer Phone Number | Yes | `"555-0101"` |
  | `dateOfBirth` | String | Customer Date of Birth | Yes | `"1992-04-15"` |
  | `email` | String | Contact Email Address | Yes | `"info@apexdesigns.example"` |
  | `address` | String | Full Delivery / Billing Address | Yes | `"101 Creative Blvd, Suite 400, Chicago, IL"` |
  | `createdAt` | Integer (Long) | Epoch Milliseconds timestamp | Yes | `1731600000000` |

#### Collection 2: `users`
- **Document ID**: Firebase Auth UID
- **Fields**:
  - `email`: String (e.g. `"staff@printshop.com"`)
  - `displayName`: String (e.g. `"Sarah Jenkins"`)
  - `createdAt`: Long

---

### 17. FIREBASE INTEGRATION
Firebase integration provides high-availability cloud infrastructure:
1. **Google Services Plugin**: Configured in root and app `build.gradle.kts` via `id("com.google.gms.google-services")`.
2. **`google-services.json`**: Places API keys, OAuth clients, and storage bucket endpoints into the compilation path.
3. **Firebase Authentication Implementation**:
   - `createUserWithEmailAndPassword(email, password)`: Creates staff credentials.
   - `signInWithEmailAndPassword(email, password)`: Authenticates staff sessions.
   - `sendPasswordResetEmail(email)`: Dispatches recovery emails.
4. **Cloud Firestore Implementation**:
   - `collection("customers").addSnapshotListener`: Establishes real-time reactive data pipeline.
   - Batch writes (`db.batch().commit()`): Executes multi-document atomic sample seeding.
5. **Security Rules (`firestore.rules`)**:
   - Blocks unauthorized access (`request.auth != null`).
   - Validates incoming data types and string lengths before writes commit.

---

### 18. SCREEN-BY-SCREEN DESCRIPTION
1. **Login & Sign-Up Screens (`LoginScreen`, `SignUpScreen`, `ForgotPasswordScreen`)**:
   - Features PrintShop branding, CMYK-styled logo, and email/password text fields.
   - Password visibility toggle icon.
   - Real-time client-side validation (RFC-compliant email check, 6-char password minimum).
   - "Forgot Password?" dialog providing password reset email dispatches.
2. **Dashboard Screen (`DashboardScreen`)**:
   - Staff greeting displaying active user email and initials avatar.
   - Primary metric card showing real-time **Total Customers** count.
   - Quick action cards for **Add Customer** and **View Customers**.
   - One-tap **Seed 10 Customers** button populating realistic print shop accounts without duplicates.
   - Preview list of recently registered customers.
   - Safe sign-out button displaying confirmation prompt.
3. **Customer Registration Screen (`AddCustomerScreen`)**:
   - Input fields: Full Name, Phone, Date of Birth (interactive date picker), Email, Full Address.
   - Inline error labels beneath invalid fields.
   - Save and Cancel/Reset buttons with animated loading progress indicator.
4. **Customer Directory Screen (`CustomerListScreen`)**:
   - Full-text search bar with live filtering by name, phone, or email.
   - Count banner (e.g., "Showing all 10 customers" or "Found 2 matching 'Apex'").
   - Scrollable `LazyColumn` of `CustomerCard` components with 2-letter avatar badges.
   - Floating Action Button (+) navigating directly to registration.
   - Empty state view when no records match.
5. **Customer Details Screen (`CustomerDetailScreen`)**:
   - Large avatar banner, client ID, and formatted registration date.
   - Information cards with phone, email, birthdate, and full street address.
   - Native Action Buttons:
     - **Call**: Directly launches phone dialer (`Intent.ACTION_DIAL`).
     - **Email**: Directly opens email app (`Intent.ACTION_SENDTO`).
   - Edit and Delete action buttons.
6. **Edit Customer Screen (`EditCustomerScreen`)**:
   - Pre-populates all existing customer information.
   - Allows updates to name, phone, DOB, email, and address.
   - Commits changes to Cloud Firestore with form validation.
7. **Delete Confirmation Dialog (`DeleteConfirmationDialog`)**:
   - Modal alert dialog warning the user of permanent deletion to prevent accidental loss.

---

### 19. TESTING AND TEST CASES

#### Test Methodology:
The application underwent unit testing, integration testing, and manual exploratory testing across Android emulators (API 34, API 30, API 24).

#### Academic Test Cases Table:
| Test Case ID | Test Description | Input Data | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| TC-01 | Staff Registration with Valid Credentials | Email: `staff1@printshop.com`<br>Pass: `secret123` | Account created; user routed to Dashboard | User created and routed to Dashboard | PASS |
| TC-02 | Staff Registration with Weak Password | Email: `test@shop.com`<br>Pass: `123` | Validation error: "Password must be at least 6 characters" | Error displayed inline; submit blocked | PASS |
| TC-03 | Staff Login with Invalid Email Format | Email: `bademail`<br>Pass: `secret123` | Validation error: "Please enter a valid email address" | Error displayed inline; submit blocked | PASS |
| TC-04 | Password Reset Request | Email: `staff1@printshop.com` | Reset email dispatched; confirmation displayed | Confirmation view displayed | PASS |
| TC-05 | Customer Registration - Empty Name | Name: `""`<br>Phone: `555-0101` | Validation error: "Full Name is required" | Error banner shown; submit blocked | PASS |
| TC-06 | Customer Registration - Invalid Phone | Phone: `abc-123` | Validation error: "Invalid phone format" | Error displayed; submit blocked | PASS |
| TC-07 | Date of Birth Picker Modal | Tap DOB field; pick 1990-11-08 | Selected date formatted as `YYYY-MM-DD` | Date displayed in input field | PASS |
| TC-08 | Customer Creation in Firestore | Valid Customer Object | Record added to Firestore; ID generated; list updated | Document stored; appears instantly in list | PASS |
| TC-09 | Seed 10 Dummy Customers | Tap "Seed 10 Customers" button | 10 sample customers added with IDs `seed_cust_001..010` | 10 records added; snackbar displayed | PASS |
| TC-10 | Duplicate Seed Prevention | Tap "Seed 10 Customers" twice | Second run overwrites deterministic IDs without creating duplicate records | Total count remains 10; no duplicate entries | PASS |
| TC-11 | Real-time Search by Name | Query: `"Apex"` | Only records matching "Apex" displayed in list | 1 matching record shown | PASS |
| TC-12 | Real-time Search by Phone | Query: `"555-0103"` | Only records matching phone displayed | 1 matching record shown | PASS |
| TC-13 | Real-time Search by Email | Query: `"@metrouniv"` | Records matching email domain displayed | 1 matching record shown | PASS |
| TC-14 | Phone Call Intent | Tap "Call" on Customer Details | Android phone dialer launched with customer number | Dialer launched with number filled | PASS |
| TC-15 | Send Email Intent | Tap "Email" on Customer Details | Default email app opened with customer email | Email app opened with "To" field populated | PASS |
| TC-16 | Edit Customer Record | Modify Phone to `555-9999` | Firestore updated; details view reflects change | Record updated; change reflected | PASS |
| TC-17 | Customer Deletion - Cancel | Tap Delete; click "Cancel" | Dialog dismissed; record preserved | Customer remains in database | PASS |
| TC-18 | Customer Deletion - Confirm | Tap Delete; click "Delete" | Document deleted in Firestore; user routed back | Document removed; list refreshes | PASS |
| TC-19 | Offline Persistence | Disconnect network; browse list | Cached customers visible and searchable | Records remain accessible from cache | PASS |

---

### 20. RESULTS
- The application built cleanly using Android Studio and Gradle with zero compiler warnings or syntax errors.
- Authentication workflows reliably handle staff sessions, registration, and password recovery.
- Cloud Firestore synchronization occurs with sub-second latency across client devices.
- The 10 realistic sample customers provide an immediate, rich dataset for evaluation without manual data entry.
- The UI conforms strictly to Material 3 standards with accessible typography and high contrast ratios.

---

### 21. LIMITATIONS
- **Single Currency & Region Formatting**: The application currently assumes standard US phone formatting and English language localization.
- **Third-Party Email Dependency**: Password reset functionality requires an active internet connection to contact Firebase Authentication endpoints.
- **Image Uploads**: Customer avatar images are currently generated from initials rather than custom uploaded company logo images.

---

### 22. FUTURE ENHANCEMENTS
1. **Camera & Gallery Integration**: Allow staff to attach customer logo files and artwork previews via Firebase Cloud Storage.
2. **Order History Subcollection**: Add a nested `orders` collection under each customer to track print jobs, paper types (glossy, matte), quantities, and proofs.
3. **Automated SMS & WhatsApp Notifications**: Integrate Twilio or WhatsApp Business API to dispatch automated job completion alerts to customers.
4. **Barcode / QR Code Generation**: Generate printable QR codes for customer accounts to affix directly onto physical print job boxes.
5. **Role-Based Access Control (RBAC)**: Distinguish between Admin, Sales Staff, and Press Operators with granular permissions.

---

### 23. CONCLUSION
The **PrintShop Customer Manager** Android application delivers a modern, robust, and scalable solution for managing printing shop customer data. By leveraging Kotlin, Jetpack Compose, Material 3, and Google Firebase, the project successfully bridges the gap between physical production workflows and digital client management. The implementation of MVVM architecture ensures high maintainability, clean testability, and seamless extensibility for enterprise printing production systems.

---

### 24. REFERENCES
1. Google Developers, *Android Jetpack Compose Documentation*, [https://developer.android.com/jetpack/compose](https://developer.android.com/jetpack/compose).
2. Google Developers, *Guide to App Architecture*, [https://developer.android.com/topic/architecture](https://developer.android.com/topic/architecture).
3. Google Firebase, *Firebase Authentication Documentation*, [https://firebase.google.com/docs/auth](https://firebase.google.com/docs/auth).
4. Google Firebase, *Cloud Firestore Documentation*, [https://firebase.google.com/docs/firestore](https://firebase.google.com/docs/firestore).
5. Material Design 3 Guidelines, *Material You for Android*, [https://m3.material.io](https://m3.material.io).
6. Gamma, E., Helm, R., Johnson, R., & Vlissides, J., *Design Patterns: Elements of Reusable Object-Oriented Software*, Addison-Wesley, 1994.
7. Kotlin Foundation, *Kotlin Language Documentation*, [https://kotlinlang.org/docs](https://kotlinlang.org/docs).

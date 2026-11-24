# Firebase Cloud Firestore Setup & Configuration Guide

## Overview
**PrintShop Customer Manager** integrates Firebase for:
1. **Firebase Authentication** (Email & Password staff login, account creation, password reset)
2. **Cloud Firestore** (Real-time NoSQL cloud database storing customer records under the `customers` collection)

---

## Step-by-Step Firebase Console Configuration

### 1. Create a Firebase Project
1. Navigate to the [Firebase Console](https://console.firebase.google.com/).
2. Click **Add Project** (or **Create a project**).
3. Enter Project Name: `PrintShop-Customer-Manager` (or custom name).
4. Google Analytics can be enabled or disabled as preferred. Click **Create Project**.

---

### 2. Register Android Application
1. On project overview, click the **Android** icon (`</>`) to add an Android app.
2. Enter the Package Name:
   ```
   com.example.printshopmanager
   ```
3. Enter App nickname: `PrintShop Manager`
4. (Optional) Enter Debug signing certificate SHA-1 from your local keystore.
5. Click **Register App**.

---

### 3. Download `google-services.json`
1. Download the generated `google-services.json` file.
2. Copy and replace the existing file at:
   ```
   PrintShopCustomerManager/app/google-services.json
   ```

---

### 4. Enable Firebase Authentication
1. In Firebase Console left menu, navigate to **Build > Authentication**.
2. Click **Get Started**.
3. Under the **Sign-in method** tab, select **Email/Password**.
4. Switch **Enable** to ON (leave Email link disabled unless desired).
5. Click **Save**.
6. (Optional) Create a test staff account under the **Users** tab (e.g. `staff@printshop.com` / `password123`).

---

### 5. Create Cloud Firestore Database
1. Navigate to **Build > Firestore Database**.
2. Click **Create Database**.
3. Choose a database location close to your region (e.g., `nam5 (us-central)` or `asia-south1`).
4. Choose **Start in production mode** (or test mode, but replace rules immediately).
5. Click **Enable**.

---

### 6. Deploy Firestore Security Rules
1. In Firestore Database, open the **Rules** tab.
2. Paste the contents of `firestore.rules` from this project:
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /customers/{customerId} {
         allow read, write: if request.auth != null;
       }
       match /users/{userId} {
         allow read, write: if request.auth != null && request.auth.uid == userId;
       }
     }
   }
   ```
3. Click **Publish**.

---

### 7. Populate Dummy Customer Data
You have two easy ways to populate the 10 dummy customers:
- **Option A (In-App)**: Run the application, log in, and tap the **"Seed 10 Customers"** button directly on the Dashboard.
- **Option B (Python CLI)**:
  1. Go to Firebase Console > Project Settings > Service accounts.
  2. Click **Generate new private key** and save as `serviceAccountKey.json`.
  3. Run:
     ```bash
     pip install firebase-admin
     python Project_Report_Materials/seed_firestore.py --service-account serviceAccountKey.json
     ```

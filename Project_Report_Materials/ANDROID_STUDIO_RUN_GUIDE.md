# Android Studio Execution & Build Guide

## Requirements
- **Android Studio**: Android Studio Hedgehog (2023.1.1), Iguana, Jellyfish, or newer.
- **JDK**: Java Development Kit 17 or 21 (bundled with modern Android Studio as `jbr`).
- **Android SDK**: Compile SDK 34, Min SDK 24 (Android 7.0 Nougat or newer).
- **Gradle**: 8.4 (handled automatically by the included Gradle wrapper).

---

## Opening the Project in Android Studio

1. Launch **Android Studio**.
2. Select **Open** from the Welcome screen (or **File > Open** if an existing project is open).
3. Navigate to and select the directory:
   ```
   PrintShopCustomerManager
   ```
4. Click **OK** / **Open**.
5. Wait for Android Studio to initialize and complete the initial Gradle Sync.

---

## Running on an Android Emulator or Physical Device

### 1. Setting up an Emulator (AVD)
1. Open **Tools > Device Manager** in Android Studio.
2. Click **Create Device**.
3. Choose a device profile (e.g., **Pixel 7** or **Pixel 8**).
4. Select a system image with **API 33 or 34** (Google Play / Google APIs x86_64).
5. Click **Next**, name the virtual device, and click **Finish**.

### 2. Running the App
1. Select your target device / emulator in the run configuration bar at the top.
2. Ensure the active configuration is `:app`.
3. Click the green **Run** button (or press `Shift + F10`).
4. The Gradle build will assemble the debug APK and launch the app on the emulator.

---

## Building the Release or Debug APK

### Via Android Studio Menu:
- Click **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
- Once finished, click **locate** in the notification to retrieve `app-debug.apk`.

### Via Terminal / Command Line:
```bash
# On Windows PowerShell / Command Prompt:
.\gradlew.bat assembleDebug

# Output APK path:
# app/build/outputs/apk/debug/app-debug.apk
```

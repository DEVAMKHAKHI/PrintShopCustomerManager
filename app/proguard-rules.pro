# ProGuard rules for PrintShop Customer Manager
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <fields>;
    @com.google.firebase.firestore.DocumentId <fields>;
}
-keep class com.example.printshopmanager.model.** { *; }

# Add project specific ProGuard rules here.
# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Moshi & Retrofit
-keepclassmembers class * {
    @com.squareup.moshi.* <methods>;
    @com.squareup.moshi.* <fields>;
}
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers enum * { *; }

# Google Play Billing
-keep class com.android.billingclient.api.** { *; }

# OkHttp & Coroutines
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Data Models
-keep class com.example.data.** { *; }
-keep class com.example.ai.** { *; }

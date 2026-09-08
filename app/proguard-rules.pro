# Maintain line numbers and source file attributes for crash analysis
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep DRG Shared Domain Models & Enums
-keep class com.example.shared.models.** { *; }
-keepclassmembers enum com.example.shared.models.** { *; }

# Keep Room Database, Entities, and DAOs
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep class androidx.room.** { *; }

# Keep Moshi Kotlin models & adapters
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keepattributes *JsonQualifier*

# Keep Retrofit & OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Keep Kotlin Coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep Serialization
-dontwarn kotlinx.serialization.**
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }

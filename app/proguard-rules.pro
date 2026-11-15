# Maintain line numbers and source file attributes for crash analysis
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,*JsonQualifier*

# Keep DRG Shared Domain Models, Update Manifest Models, & Enums
-keep class com.example.shared.models.** { *; }
-keepclassmembers enum com.example.shared.models.** { *; }
-keep class com.example.core.update.** { *; }
-keep class com.example.core.p2p.** { *; }

# Keep Room Database, Entities, and DAOs
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep class androidx.room.** { *; }

# Keep Moshi Kotlin models & adapters
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
    @com.squareup.moshi.JsonClass <fields>;
}

# Keep Retrofit & OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
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

# Keep Credential Manager & Google Identity Services (Google Sign-In)
-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }
-dontwarn androidx.credentials.**

# Keep Firebase Auth, Firestore, App Check, AI Logic, Messaging
-keep class com.google.firebase.** { *; }
-keep class com.google.firebase.messaging.** { *; }
-keep class com.google.android.recaptcha.** { *; }
-dontwarn com.google.firebase.**

# Keep Google Maps & Location
-keep class com.google.android.gms.maps.** { *; }
-keep class com.google.maps.android.** { *; }
-dontwarn com.google.android.gms.**

# Keep Apache Commons Compress (BSDIFF40 / BZip2 OTA Delta Patch) & ignore optional codecs
-keep class org.apache.commons.compress.compressors.bzip2.** { *; }
-dontwarn org.tukaani.xz.**
-dontwarn com.github.luben.zstd.**
-dontwarn org.brotli.dec.**
-dontwarn org.objectweb.asm.**
-dontwarn org.apache.commons.codec.**

# Keep WebRTC (required for P2P Audio & DataChannel)
-keep class org.webrtc.** { *; }
-dontwarn org.webrtc.**
-keepclassmembers class org.webrtc.** { *; }

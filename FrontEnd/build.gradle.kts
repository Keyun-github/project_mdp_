plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false

    // TAMBAHKAN PLUGIN KSP DI SINI
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}
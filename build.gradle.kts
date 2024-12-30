// build.gradle.kts (nivel de proyecto)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Agregar el plugin de Google Services para gestionarlo correctamente
    id("com.google.gms.google-services") version "4.3.15" apply false
}


buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Clasepath para Google Services
        classpath("com.google.gms:google-services:4.3.15")
    }
}

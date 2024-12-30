plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services") // Aplicar el plugin de Google Services
}

android {
    namespace = "tarea3.perez_arias_cristian_pmdm_tarea_3"
    compileSdk = 34

    defaultConfig {
        applicationId = "tarea3.perez_arias_cristian_pmdm_tarea_3"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildToolsVersion = "34.0.0"
}

dependencies {
    // Firebase BOM para manejar las versiones de Firebase automáticamente
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-appcheck-safetynet:16.0.0")
    // Dependencias de Firebase
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database:21.0.0") // Firebase Realtime Database
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("com.google.android.gms:play-services-auth:21.3.0")

    // Dependencia para Google Play Services
    implementation("com.google.android.gms:play-services-auth:21.3.0")

    // Flexbox layout
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Dependencias de AndroidX
    val fragment_version = "1.8.3"
    implementation("androidx.fragment:fragment:$fragment_version")
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Dependencias de testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Aplicar el plugin de Google Services
apply(plugin = "com.google.gms.google-services")

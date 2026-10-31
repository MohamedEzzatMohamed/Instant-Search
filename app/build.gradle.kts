import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.firebase.appdistribution)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.mezzat.instantsearch"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.mezzat.instantsearch"
        minSdk = 26
        targetSdk = 36
        versionCode = 3
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            optimization {
                enable = false
            }
            matchingFallbacks.addAll(listOf("release", "debug"))
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            matchingFallbacks.addAll(listOf("debug", "release"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Instant Search Dev")
            buildConfigField("String", "BASE_URL", "\"https://rickandmortyapi.com/api/character/\"")
            buildConfigField("String", "ENV_NAME", "\"development\"")
            firebaseAppDistribution {
                releaseNotes="Release notes for dev version"
                testers="zonesmart630@gmail.com"
            }
        }
        create("prod") {
            dimension = "environment"
            resValue("string", "app_name", "Instant Search")
            buildConfigField("String", "BASE_URL", "\"https://rickandmortyapi.com/api/character/\"")
            buildConfigField("String", "ENV_NAME", "\"production\"")
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotesFile = "/path/to/releasenotes.txt"
                testers = "zonesmart630@gmail.com"
            }
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("io.ktor:ktor-client-mock:3.1.0")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val media3 = "1.4.1"

android {
    namespace = "app.flowplay"
    compileSdk = 35
    defaultConfig {
        applicationId = "app.flowplay"
        minSdk = 29
        targetSdk = 35
        versionCode = (System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull() ?: 1)
        versionName = "1." + (System.getenv("GITHUB_RUN_NUMBER") ?: "0")
        resourceConfigurations += listOf("en", "ar")
    }
    signingConfigs {
        create("flow") {
            storeFile = file("flow.jks")
            storePassword = System.getenv("FLOW_STORE_PASSWORD") ?: error("FLOW_STORE_PASSWORD is required for release builds")
            keyAlias = System.getenv("FLOW_KEY_ALIAS") ?: error("FLOW_KEY_ALIAS is required for release builds")
            keyPassword = System.getenv("FLOW_KEY_PASSWORD") ?: error("FLOW_KEY_PASSWORD is required for release builds")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("flow")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-opt-in=androidx.media3.common.util.UnstableApi"
    }
    lint { checkReleaseBuilds = false }
    buildFeatures { compose = true }
    packaging {
        resources { excludes += listOf("/META-INF/{AL2.0,LGPL2.1}", "DebugProbesKt.bin", "kotlin-tooling-metadata.json", "/META-INF/*.version", "/META-INF/proguard/**") }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("androidx.media3:media3-exoplayer:$media3")
    implementation("androidx.media3:media3-exoplayer-hls:$media3")
    implementation("androidx.media3:media3-exoplayer-dash:$media3")
    implementation("androidx.media3:media3-ui:$media3")
    implementation("androidx.media3:media3-session:$media3")
}

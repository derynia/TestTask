plugins {
    id("com.android.application")
//    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = ConfigData.compileSdkVersion
    buildToolsVersion = ConfigData.buildToolsVersion

    defaultConfig {
        applicationId = "com.android.testtask"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core
    implementation (Deps.coreKtx)
    implementation (Deps.appCompat)
    implementation (Deps.materialDesign)
    implementation (Deps.constraintLayout)

//    // Firebase
//    implementation(platform(Deps.firebaseBom))
//    implementation(Deps.analytics)

    // Navigation
    implementation (Deps.navigationFragmentKtx)
    implementation (Deps.navigationUiKtx)

    // Hilt
    implementation(Deps.hilt)
    kapt(Deps.hiltCompiler)

    // Tests
    testImplementation (Deps.junit)
    androidTestImplementation (Deps.extJunit)
    androidTestImplementation (Deps.espresso)

    // ROOM
    api(Deps.roomRuntime)
    annotationProcessor(Deps.roomCompiler)
    kapt(Deps.roomCompiler)
    implementation(Deps.roomCore)
}
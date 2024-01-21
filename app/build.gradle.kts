buildscript {
    dependencies {
        classpath(Libraries.Navigation.navigationSafeArgs)
    }
}

plugins {
    id(Plugins.application)
    id(Plugins.jetBrains)
    id(Plugins.navigationSafeArgs)
    id(Plugins.parcelize)
    id(Plugins.ksp)
}

android {
    namespace = "com.example.calendarsimbirsoft"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.calendarsimbirsoft"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(Libraries.Dagger.dagger)
    ksp (Libraries.Dagger.daggerCompiler)
    implementation(Libraries.Room.roomRuntime)
    implementation(Libraries.Room.roomKtx)
    annotationProcessor(Libraries.Room.roomCompiler)
    ksp(Libraries.Room.roomCompiler)
    implementation(Libraries.DateTime.dateTime)
    implementation(Libraries.Adapter.adapterDelegate)
    implementation(Libraries.Navigation.navigationFragment)
    implementation(Libraries.AndroidX.viewModel)
    implementation(Libraries.AndroidX.viewBinding)
    implementation(Libraries.AndroidX.fragment)
    implementation(Libraries.AndroidX.core)
    implementation(Libraries.AndroidX.appCompat)
    implementation(Libraries.AndroidX.material)
    implementation(Libraries.AndroidX.constraintLayout)
    testImplementation(Libraries.AndroidX.jUnit)
    androidTestImplementation(Libraries.AndroidX.extjUnit)
    androidTestImplementation(Libraries.AndroidX.espresso)
}
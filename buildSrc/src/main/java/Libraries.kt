object Libraries {

    object Adapter {
        const val adapterDelegate = "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:4.3.2"
    }

    object Room {
        const val roomVersion = "2.6.1"
        const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
        const val roomKtx = "androidx.room:room-ktx:$roomVersion"
        const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    }

    object DateTime {
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.1"
    }

    object Dagger {
        const val daggerVersion = "2.49"
        const val dagger = "com.google.dagger:dagger:$daggerVersion"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    }

    object Navigation {
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:2.7.6"
        const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6"
    }

    object AndroidX {
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2"
        const val viewBinding = "com.github.kirich1409:viewbindingpropertydelegate-full:1.5.9"
        const val fragment = "androidx.fragment:fragment-ktx:1.6.2"
        const val core = "androidx.core:core-ktx:1.12.0"
        const val appCompat = "androidx.appcompat:appcompat:1.6.1"
        const val material = "com.google.android.material:material:1.11.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val jUnit = "junit:junit:4.13.2"
        const val extjUnit = "androidx.test.ext:junit:1.1.5"
        const val espresso = "androidx.test.espresso:espresso-core:3.5.1"
    }
}

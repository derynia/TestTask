/**
 * To define plugins
 */
object BuildPlugins {
    val secretGradle by lazy {"com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${Versions.secretPluginVersion}"}
    val firebase by lazy { "com.google.gms:google-services:${Versions.fireBaseVersion}" }
    val android by lazy { "com.android.tools.build:gradle:${Versions.gradlePlugin}" }
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" }
    val hilt by lazy {"com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"}
    val crashLyticsPlugin by lazy { "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashLyticsVersion}" }
}

/**
 * To define dependencies
 */
object Deps {
    // Firebase
    val firebaseBom by lazy {"com.google.firebase:firebase-bom:${Versions.fireBaseBOMVersion}"}
    val analytics by lazy {"com.google.firebase:firebase-analytics-ktx"}
    val crashLytics by lazy {"com.google.firebase:firebase-crashlytics-ktx"}
    val fireStore by lazy {"com.google.firebase:firebase-firestore-ktx"}

    // Core
    val coreKtx by lazy { "androidx.core:core-ktx:${Versions.ktx}" }
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}" }
    val extJunit by lazy { "androidx.test.ext:junit:${Versions.extJunit}" }
    val espresso by lazy { "androidx.test.espresso:espresso-core:${Versions.espresso}" }
    val junit by lazy { "junit:junit:${Versions.junit}" }

    val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.appcompat}" }
    val maps by lazy { "com.google.android.gms:play-services-maps:${Versions.mapsVersion}"}
    val location by lazy { "com.google.android.gms:play-services-location:${Versions.locationVersion}"}
    val viewBinding by lazy {"androidx.databinding:viewbinding:${Versions.viewBinding}"}
    val materialDesign by lazy { "com.google.android.material:material:${Versions.material}" }
    val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}" }
    val navigationFragmentKtx by lazy { "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}" }
    val navigationUiKtx by lazy { "androidx.navigation:navigation-ui-ktx:${Versions.navigation}" }
    val coroutines by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}" }

    // Hilt
    val hilt by lazy {"com.google.dagger:hilt-android:${Versions.hilt}"}
    val hiltCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hilt}" }

    // Room
    val roomCore by lazy { "androidx.room:room-ktx:${Versions.room}" }
    val roomRuntime by lazy { "androidx.room:room-runtime:${Versions.room}" }
    val roomCompiler by lazy { "androidx.room:room-compiler:${Versions.room}" }
}
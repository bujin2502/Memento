plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "hr.foi.rampu.memento"
    compileSdk = 36

    defaultConfig {
        applicationId = "hr.foi.rampu.memento"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    val roomVersion = "2.8.0"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)

    implementation("androidx.room:room-runtime:${roomVersion}")
    kapt ("androidx.room:room-compiler:$roomVersion")
    annotationProcessor ("androidx.room:room-compiler:$roomVersion")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.2.0")
    implementation("com.squareup.picasso:picasso:2.8")

    implementation("androidx.core:core:1.6.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
}
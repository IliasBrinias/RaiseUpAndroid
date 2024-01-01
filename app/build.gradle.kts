plugins {
    id("com.android.application")
}

android {
    namespace = "com.unipi.msc.riseupandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.unipi.msc.riseupandroid"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    //Shared Pref
    implementation("androidx.preference:preference:1.2.1")
    //KeyboardVisibilityEvent
    implementation("net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:3.0.0-RC3")
    //okhttp
    implementation("com.squareup.okhttp3:okhttp:3.10.0")
    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    //flexbox
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    //Line Chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    //Color Picker
    implementation("com.github.skydoves:colorpickerview:2.3.0")
    implementation("com.github.madrapps:pikolo:2.0.2")
    // Glide
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.4.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
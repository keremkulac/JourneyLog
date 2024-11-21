plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.safeArgs)
    alias(libs.plugins.kapt)
    alias(libs.plugins.mapsplatform)
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.kotlinParcelize)

}

android {
    namespace = "com.keremkulac.journeylog"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.keremkulac.journeylog"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["GOOGLE_MAP_API_KEY"] = "AIzaSyAq3bXhYQfh1GxknLhjN8bxmXvFDfqAP18"
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

    packaging {
        resources {
            excludes.add("/META-INF/gradle/incremental.annotation.processors")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.hilt)
    implementation(libs.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment)
    implementation(libs.fragment)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.common)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.core.splashscreen)
    implementation(libs.circleimageview)
    implementation(libs.glide)
    implementation(libs.androidx.core)
    implementation(libs.androidx.ktx.core)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.base)
    implementation(libs.androidx.runtime.saved.instance.state)
    implementation(libs.play.services.cast.framework)
    implementation(libs.androidx.viewpager2)
    implementation(libs.circleindicator)
    implementation(libs.datastore.preferences)
    implementation(libs.work.manager)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.bom)
    implementation(libs.firebase.storage)
    implementation(libs.googleid)
    kapt(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
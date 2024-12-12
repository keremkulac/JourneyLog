import java.util.Properties

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
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").reader())
        manifestPlaceholders["GOOGLE_MAP_API_KEY"] =
            "\"${properties.getProperty("GOOGLE_MAP_API_KEY")}\""
        buildConfigField(
            "String",
            "RAPID_API_KEY",
            "\"${properties.getProperty("RAPID_API_KEY")}\""
        )


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

    implementation(libs.androidx.core.ktx) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.appcompat) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.material) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.activity) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.constraintlayout) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.hilt) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.hilt.compiler) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.retrofit) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.retrofit.gson) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.gson) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.navigation.ui) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.navigation.fragment) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.fragment) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.lifecycle.livedata) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.lifecycle.common) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.lifecycle.viewmodel) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.okhttp.logging.interceptor) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.core.splashscreen) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.circleimageview) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.glide) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.core) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.ktx.core) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.play.services.maps) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.play.services.location) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.play.services.auth) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.play.services.base) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.runtime.saved.instance.state) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.play.services.cast.framework) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.viewpager2) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.circleindicator) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.work.manager) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.firebase.auth) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.firebase.firestore) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.firebase.bom) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.firebase.storage) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.googleid) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.coroutine) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.room.common) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.room.runtime) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.room.compiler) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.room.ktx) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.mpandroidchart) {
        exclude(group = "com.intellij", module = "annotations")
    }
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.junit) {
        exclude(group = "com.intellij", module = "annotations")
    }
    androidTestImplementation(libs.androidx.junit) {
        exclude(group = "com.intellij", module = "annotations")
    }
    androidTestImplementation(libs.androidx.espresso.core) {
        exclude(group = "com.intellij", module = "annotations")
    }
}
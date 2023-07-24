import org.jetbrains.kotlin.fir.scopes.debugCollectOverrides

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.google.gms.google-services")
    id( "com.google.firebase.crashlytics")


}

kotlin {
    android()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(deps.decompose)
                with(deps.koin) {
                    api(android)
                }
            }
        }
    }
}







android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.presta.customer.codo"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        applicationId = "com.presta.customer.codo"
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

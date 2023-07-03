buildscript {
    dependencies {
        classpath(deps.gms.google.services)
        classpath(deps.jetbrains.kotlinx.binaryCompatibilityValidator)
        classpath(deps.parcelizeDarwin.gradlePlug)
        classpath(deps.moko.resources.generator)
        classpath(deps.kotlin.kotlinGradlePlug)
        classpath("com.android.tools.build:gradle:3.4.0")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
    }
}

plugins {

    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("app.cash.sqldelight").apply(false)

}

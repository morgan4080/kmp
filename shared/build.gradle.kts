plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization") version "1.9.0"
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.arkivanov.parcelize.darwin")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-resources")
    id("app.cash.sqldelight")
    id("com.google.firebase.crashlytics")
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    androidTarget()
    ios()

    cocoapods {
        version = "1.0.1"
        summary = "Presta Customer Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true

            export(deps.decompose)

            export(deps.essenty.lifecycle)

            // Optional, only if you need state preservation on Darwin (Apple) targets
            export(deps.essenty.stateKeeper)

            // Optional, only if you need state preservation on Darwin (Apple) targets
            export(deps.parcelizeDarwin.runtime)

            export(deps.moko.resources)

            export(deps.moko.resources.graphics)

            export("com.mohamedrejeb.calf:calf-ui:0.1.1")
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                with(compose) {
                    implementation(runtime)
                    implementation(foundation)
                    implementation(material)
                    implementation(material3)
                    implementation(components.resources)
                    implementation(materialIconsExtended)
                }

                implementation(deps.decompose)
                implementation(deps.decompose.jetbrains)
                api(deps.essenty.lifecycle)
                api(deps.essenty.stateKeeper)

                // Ktor
                with(deps.ktor) {
                    api(clientCore)
                    api(serializationKotlinxJson)
                    api(clientContentNegotiation)
                    api(clientLogging)
                }

                // Logback for ktor logging
                implementation(deps.logbackClassic)

                // SqlDelight
                with(deps.sqlDelight) {
                    api(coroutinesExtensions)
                    api(primitiveAdapters)
                }

                // Koin
                with(deps.koin) {
                    api(core)
                    api(test)
                }

                // KotlinX Serialization Json
                implementation(deps.jetbrains.kotlinx.kotlinxSerializationJson)

                // Coroutines
                implementation(deps.jetbrains.kotlinx.kotlinxCoroutinesCore)

                // MVIKotlin
                api(deps.mvikotlin)
                api(deps.mviKotlin.mvikotlinMain)
                api(deps.mviKotlin.mvikotlinExtensionsCoroutines)

                // moko resources
                api(deps.moko.resources)
                api(deps.moko.resources.compose)

                // settings
                implementation(deps.russhwolf.settings.core)
                implementation(deps.russhwolf.settings.serialization)

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("com.github.ln-12:multiplatform-connectivity-status:1.2.0")
                api("io.github.qdsfdhvh:image-loader:1.4.1")
                implementation("com.moriatsushi.insetsx:insetsx:0.1.0-alpha10")
                api("com.mohamedrejeb.calf:calf-ui:0.1.1")
            }
        }

        val androidMain by getting {
            dependencies {
                with(deps.androidx){
                    api(activity.activityCompose)
                    api(appcompat.appcompat)
                    api(core.coreKtx)
                }

                // Ktor
                implementation(deps.ktor.clientAndroid)

                // SqlDelight
                implementation(deps.sqlDelight.androidDriver)

                // Koin
                implementation(deps.koin.android)

                implementation("com.google.android.gms:play-services-auth:20.7.0")

                implementation("com.google.android.gms:play-services-auth-api-phone:18.0.1")

                implementation("androidx.biometric:biometric:1.2.0-alpha05")
                //Firebase
                implementation ("com.google.firebase:firebase-bom:32.1.1")
                implementation (deps.analytics.firebase)
                implementation (deps.crashlytics.firebase)
                implementation("com.googlecode.libphonenumber:libphonenumber:8.2.0")
            }

            dependsOn(commonMain)
        }

        val iosMain by getting {
            dependencies {
                // Ktor
                implementation(deps.ktor.clientDarwin)

                // SqlDelight
                implementation(deps.sqlDelight.nativeDriver)

                api(deps.decompose)

                api(deps.essenty.lifecycle)
            }

            dependsOn(commonMain)
        }

        multiplatformResources {
            multiplatformResourcesPackage = "com.presta.customer"
            multiplatformResourcesSourceSet = "commonMain"
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.presta.customer"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
dependencies {
    implementation("com.google.android.material:material:1.9.0")
}

sqldelight {
    databases {
        create("PrestaCustomerDB") {
            packageName.set("com.presta.customer.database")
        }
    }
}



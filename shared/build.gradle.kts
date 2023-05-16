plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization") version "1.8.20"
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.arkivanov.parcelize.darwin")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-resources")
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "15.2"
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
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.github.qdsfdhvh:image-loader:1.4.1")
                with(compose) {
                    implementation(runtime)
                    implementation(foundation)
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
                implementation(deps.moko.resources.test)

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
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                // Ktor
                implementation(deps.ktor.clientDarwin)

                // SqlDelight
                implementation(deps.sqlDelight.nativeDriver)

                api(deps.decompose)

                api(deps.essenty.lifecycle)
            }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

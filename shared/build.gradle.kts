import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.parcelize)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.burst)
    alias(libs.plugins.metro)
}

ksp {
    arg("circuit.codegen.mode", "metro")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.unitTest)
    }
    iosArm64()
    iosSimulatorArm64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.all {
            freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
            linkerOpts("-ios_deployment_target", "15.0")
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "A toy Lightsaber app built with Compose Multiplatform"
        homepage = "https://github.com/aaalaniz/compose-lightsaber"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
                implementation(libs.circuitx.gesture.navigation)
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.androidx.datastore.core.okio)
                api(libs.kermit)
                implementation(libs.compose.colorpicker)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.compose.ui.test)
            }
        }
        androidMain {
            dependencies {
                api(libs.androidx.activity.compose)
                api(libs.androidx.appcompat)
                api(libs.androidx.core.ktx)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.compose.ui.test.junit4.android)
            }
        }
    }
    sourceSets.androidUnitTest.dependencies {
        implementation(kotlin("test"))
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "xyz.alaniz.aaron.lightsaber"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/composeResources/files")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        lint.targetSdk = (findProperty("android.targetSdk") as String).toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
    }
}

/**
 * Generate Circuit graph dependencies for each platform
 *
 * https://github.com/google/ksp/issues/567
 */
dependencies {
    add("kspCommonMainMetadata", libs.circuit.codegen)
    add("kspAndroid", libs.circuit.codegen)
    add("kspIosArm64", libs.circuit.codegen)
    add("kspIosSimulatorArm64", libs.circuit.codegen)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
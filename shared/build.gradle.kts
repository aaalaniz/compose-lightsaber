import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.parcelize)
    alias(libs.plugins.kotlin.native.cocoapods)
}

ksp {
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "kotlin_inject_anvil")
    arg("kotlin-inject-anvil-contributing-annotations", "com.slack.circuit.codegen.annotations.CircuitInject")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)

            dependencies {
                implementation(libs.androidx.compose.ui.test.junit4.android)
                debugImplementation(libs.androidx.compose.ui.test.manifest)
            }
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.unitTest)
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "A toy Lightsaber app built with Compose Multiplatform"
        homepage = "https://github.com/aaalaniz/compose-lightsaber"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.kotlin.inject.runtime.kmp)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
                implementation(libs.circuitx.gesture.navigation)
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.androidx.datastore.core.okio)
                api(libs.kermit)
                implementation(libs.anvil.kotlin.inject.runtime)
                implementation(libs.anvil.kotlin.inject.runtime.optional)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
            }
        }
        androidMain {
            dependencies {
                api(libs.androidx.activity.compose)
                api(libs.androidx.appcompat)
                api(libs.androidx.core.ktx)
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

/**
 * Generate kotlin-inject component code for each platform
 *
 * https://github.com/google/ksp/issues/567
 */
dependencies {
    add("kspCommonMainMetadata", libs.kotlin.inject.ksp)
    add("kspCommonMainMetadata", libs.anvil.kotlin.inject.compiler)
    add("kspCommonMainMetadata", libs.circuit.codegen)

    add("kspAndroid", libs.kotlin.inject.ksp)
    add("kspAndroid", libs.anvil.kotlin.inject.compiler)
    add("kspAndroid", libs.circuit.codegen)

    add("kspIosX64", libs.kotlin.inject.ksp)
    add("kspIosX64", libs.anvil.kotlin.inject.compiler)
    add("kspIosX64", libs.circuit.codegen)

    add("kspIosArm64", libs.kotlin.inject.ksp)
    add("kspIosArm64", libs.anvil.kotlin.inject.compiler)
    add("kspIosArm64", libs.circuit.codegen)

    add("kspIosSimulatorArm64", libs.kotlin.inject.ksp)
    add("kspIosSimulatorArm64", libs.anvil.kotlin.inject.compiler)
    add("kspIosSimulatorArm64", libs.circuit.codegen)
}
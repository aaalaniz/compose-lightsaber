import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.compose")
    id("lightsaber.kmp.circuit")
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.burst)
}

kotlin {
    cocoapods {
        version = "1.0.0"
        summary = "A toy Lightsaber app built with Compose Multiplatform"
        homepage = "https://github.com/aaalaniz/compose-lightsaber"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
            export(project(":core:ui"))
            export(project(":core:data"))
            export(project(":core:audio"))
            export(project(":core:motion"))
            export(project(":feature:lightsaber"))
            export(project(":feature:settings"))
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                api(project(":core:data"))
                api(project(":core:audio"))
                api(project(":core:motion"))
                api(project(":feature:lightsaber"))
                api(project(":feature:settings"))
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.androidx.datastore.core.okio)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
                implementation(libs.circuitx.gesture.navigation)
                api(libs.kermit)
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
}

android {
    namespace = "xyz.alaniz.aaron.lightsaber"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

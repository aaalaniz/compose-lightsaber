plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.compose")
    id("lightsaber.kmp.circuit")
    id("org.jetbrains.kotlin.plugin.parcelize")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.feature.settings" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                implementation(project(":core:data"))
                implementation(libs.circuitx.gesture.navigation)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
            }
        }
    }
}

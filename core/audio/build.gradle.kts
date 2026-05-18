plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.core.audio" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                implementation(libs.kermit)
                implementation(libs.compose.resources)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
            }
        }
    }
}

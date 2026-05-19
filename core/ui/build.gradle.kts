plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.compose")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.core.ui" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)
                api(libs.compose.colorpicker)
                api(libs.circuit.codegen.annotations)
                api(libs.circuit.foundation)
            }
        }
    }
}

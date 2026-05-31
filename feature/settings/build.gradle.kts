plugins {
    id("lightsaber.module")
}
lightsaber {
    compose = true
    circuit = true
    parcelize = true
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

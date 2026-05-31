plugins {
    id("lightsaber.module")
}
lightsaber {
    circuit = true
    compose = true
}
android {
    namespace = "xyz.alaniz.aaron.lightsaber.core.audio"
}
compose.resources {
    publicResClass = true
    packageOfResClass = "xyz.alaniz.aaron.lightsaber.core.audio.resources"
}
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

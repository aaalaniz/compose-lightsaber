plugins {
    id("xyz.alaniz.aaron.ccmp")
}
ccmp {

    resources {
        publicResClass = true
        packageOfResClass = "xyz.alaniz.aaron.lightsaber.core.audio.resources"
    }
}
android {
    namespace = "xyz.alaniz.aaron.lightsaber.core.audio"
}
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:platform"))
                implementation(libs.kermit)
                implementation(libs.circuit.foundation)
                implementation(libs.compose.resources)

            }
        }
    }
}

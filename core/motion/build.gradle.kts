plugins {
    id("xyz.alaniz.aaron.ccmp")
}

android { namespace = "xyz.alaniz.aaron.lightsaber.core.motion" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kermit)
                implementation(libs.circuit.foundation)
                api(project(":core:ui"))

            }
        }
    }
}

plugins {
    id("xyz.alaniz.aaron.ccmp")
}

android { namespace = "xyz.alaniz.aaron.lightsaber.core.motion" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kermit)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)

            }
        }
    }
}

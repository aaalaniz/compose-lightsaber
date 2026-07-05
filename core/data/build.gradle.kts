plugins {
    id("xyz.alaniz.aaron.ccmp")
}

android { namespace = "xyz.alaniz.aaron.lightsaber.core.data" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:platform"))
                api(project(":core:ui"))
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.circuit.foundation)
                implementation(libs.androidx.datastore.core.okio)

            }
        }
    }
}

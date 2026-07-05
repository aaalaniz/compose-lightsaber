plugins {
    id("xyz.alaniz.aaron.ccmp")
}
ccmp {
    circuit = true
}
android { namespace = "xyz.alaniz.aaron.lightsaber.core.data" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.androidx.datastore.core.okio)
                implementation(libs.circuit.foundation)
                api(libs.circuit.codegen.annotations)
            }
        }
    }
}

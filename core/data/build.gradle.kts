plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.core.data" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                api(libs.androidx.datastore.preferences.core)
                api(libs.androidx.datastore.core.okio)
                api(libs.circuit.foundation)
                api(libs.circuit.codegen.annotations)
            }
        }
    }
}

plugins {
    id("xyz.alaniz.aaron.ccmp")
}
ccmp {
    compose = true
    circuit = true
    parcelize = true
}
android { namespace = "xyz.alaniz.aaron.lightsaber.feature.lightsaber" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                implementation(project(":core:audio"))
                implementation(project(":core:motion"))
                implementation(project(":core:data"))
                api(project(":feature:settings"))
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)

            }
        }
    }
}

plugins {
    id("lightsaber.module")
}
lightsaber {
    circuit = true
}
android {
    namespace = "xyz.alaniz.aaron.lightsaber.core.audio"
    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/composeResources/files")
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

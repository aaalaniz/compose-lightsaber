plugins {
    `kotlin-dsl`
}

group = "xyz.alaniz.aaron.lightsaber.buildlogic"

dependencies {
    compileOnly(libs.plugins.kotlin.multiplatform)
    compileOnly(libs.plugins.android.library)
    compileOnly(libs.plugins.compose)
    compileOnly(libs.plugins.compose.compiler)
    compileOnly(libs.plugins.ksp)
    compileOnly(libs.plugins.metro)
}

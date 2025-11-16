plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

ksp {
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "kotlin_inject_anvil")
    arg("kotlin-inject-anvil-contributing-annotations", "com.slack.circuit.codegen.annotations.CircuitInject")
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "xyz.alaniz.aaron.lightsaber"

    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")

    defaultConfig {
        applicationId = "xyz.alaniz.aaron.Lightsaber"
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(libs.kotlin.inject.runtime.kmp)
    api(libs.circuit.codegen.annotations)
    implementation(libs.circuit.foundation)
    implementation(libs.circuitx.gesture.navigation)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.datastore.core.okio)
    api(libs.kermit)
    implementation(libs.anvil.kotlin.inject.runtime)
    implementation(libs.anvil.kotlin.inject.runtime.optional)
    implementation(libs.compose.colorpicker)
    api(libs.androidx.activity.compose)
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)

    add("ksp", libs.kotlin.inject.ksp)
    add("ksp", libs.anvil.kotlin.inject.compiler)
    add("ksp", libs.circuit.codegen)
}

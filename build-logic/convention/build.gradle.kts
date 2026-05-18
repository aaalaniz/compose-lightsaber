plugins {
    `kotlin-dsl`
}
group = "xyz.alaniz.aaron.lightsaber.buildlogic"
dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.21")
    implementation("org.jetbrains.kotlin.plugin.parcelize:org.jetbrains.kotlin.plugin.parcelize.gradle.plugin:2.3.21")
    implementation("com.android.tools.build:gradle:8.13.2")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.11.0")
    implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:2.3.21")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.2.20-2.0.4")
    implementation("dev.zacsweers.metro:dev.zacsweers.metro.gradle.plugin:1.1.1")
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xskip-metadata-version-check")
    }
}
gradlePlugin {
    plugins {
        create("kmpLibrary") {
            id = "lightsaber.kmp.library"
            implementationClass = "convention.KmpLibraryConventionPlugin"
        }
        create("kmpCompose") {
            id = "lightsaber.kmp.compose"
            implementationClass = "convention.KmpComposeConventionPlugin"
        }
        create("kmpCircuit") {
            id = "lightsaber.kmp.circuit"
            implementationClass = "convention.KmpCircuitConventionPlugin"
        }
    }
}

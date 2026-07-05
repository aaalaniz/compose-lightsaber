plugins {
    id("xyz.alaniz.aaron.ccmp") version "0.3.0" apply false
    alias(libs.plugins.kotlin.native.cocoapods).apply(false)
    alias(libs.plugins.burst).apply(false)
    alias(libs.plugins.detekt).apply(false)
    alias(libs.plugins.dependency.analysis)
}

subprojects {
    pluginManager.apply("io.gitlab.arturbosch.detekt")
    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension>("detekt") {
        autoCorrect = true
        config.setFrom(rootProject.file("config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        source.setFrom(
            fileTree("src").apply {
                include("**/*.kt")
                include("**/*.kts")
            }
        )
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "21"
    }
}

tasks.register("printNativeCacheVersions") {
    doLast {
        try {
            val clazz = Class.forName("org.jetbrains.kotlin.gradle.plugin.mpp.DisableCacheInKotlinVersion")
            clazz.enumConstants.forEach { println(it) }
        } catch (e: Exception) {
            println("Class not found: ${e.message}")
        }
    }
}

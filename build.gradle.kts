plugins {
    id("xyz.alaniz.aaron.ccmp") version "0.3.0" apply false
    alias(libs.plugins.kotlin.native.cocoapods).apply(false)
    alias(libs.plugins.burst).apply(false)
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

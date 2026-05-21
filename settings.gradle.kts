pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        google()
    }
}
rootProject.name = "Lightsaber"
include(":androidApp")
include(":shared")
include(":core:ui")
include(":core:data")
include(":core:audio")
include(":core:motion")
include(":feature:lightsaber")
include(":feature:settings")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

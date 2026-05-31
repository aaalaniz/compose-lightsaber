package convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class LightsaberModuleConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.create("lightsaber", LightsaberModuleExtension::class.java, this)

            // Base KMP library convention is always applied
            pluginManager.apply("lightsaber.kmp.library")
        }
    }
}

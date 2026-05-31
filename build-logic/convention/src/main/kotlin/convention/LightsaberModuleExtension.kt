package convention

import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import javax.inject.Inject

open class LightsaberModuleExtension @Inject constructor(private val project: Project) {
    var compose: Boolean = false
        set(value) {
            field = value
            if (value) project.pluginManager.apply("lightsaber.kmp.compose")
        }
    var circuit: Boolean = false
        set(value) {
            field = value
            if (value) project.pluginManager.apply("lightsaber.kmp.circuit")
        }
    var parcelize: Boolean = false
        set(value) {
            field = value
            if (value) project.pluginManager.apply("org.jetbrains.kotlin.plugin.parcelize")
        }
    var packageOfResClass: String? = null
        set(value) {
            field = value
            if (value != null) {
                // Ensure compose is applied first
                if (!compose) {
                    compose = true
                }
                val composeExt = project.extensions.getByType(ComposeExtension::class.java)
                val resourcesExt = composeExt.extensions.getByType(ResourcesExtension::class.java)
                resourcesExt.publicResClass = true
                resourcesExt.packageOfResClass = value
            }
        }
}

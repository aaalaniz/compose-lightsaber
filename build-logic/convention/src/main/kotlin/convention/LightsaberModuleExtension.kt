package convention

import org.gradle.api.Project
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
}

package convention
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.artifacts.VersionCatalogsExtension
class KmpCircuitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.getByType(KspExtension::class.java)
            extension.arg("circuit.codegen.mode", "metro")
            dependencies {
                val catalogs = project.rootProject.extensions.findByType(VersionCatalogsExtension::class.java)
                val libs = catalogs?.named("libs") ?: error("libs catalog not found")
                val codegen = libs.findLibrary("circuit.codegen").get()
                add("kspCommonMainMetadata", codegen)
                add("kspAndroid", codegen)
                add("kspIosArm64", codegen)
                add("kspIosSimulatorArm64", codegen)
            }
        }
    }
}

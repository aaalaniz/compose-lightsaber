package convention
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("com.google.devtools.ksp")
                apply("dev.zacsweers.metro")
                apply("org.jetbrains.kotlin.plugin.parcelize")
            }
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    @OptIn(ExperimentalKotlinGradlePluginApi::class)
                    instrumentedTestVariant {
                        sourceSetTree.set(KotlinSourceSetTree.test)
                    }
                    @OptIn(ExperimentalKotlinGradlePluginApi::class)
                    unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.unitTest)
                }
                iosArm64()
                iosSimulatorArm64()
                targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
                    binaries.all {
                        freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
                    }
                }
                jvmToolchain(21)
            }
            extensions.configure<LibraryExtension> {
                compileSdk = 35
                defaultConfig {
                    minSdk = 24
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_21
                    targetCompatibility = JavaVersion.VERSION_21
                }
            }
        }
    }
}

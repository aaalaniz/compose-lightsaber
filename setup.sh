#!/bin/bash
set -e

# Setup Build logic
mkdir -p build-logic/convention/src/main/kotlin/convention
cat << 'INNEREOF' > build-logic/settings.gradle.kts
rootProject.name = "build-logic"
include(":convention")
pluginManagement {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
INNEREOF

cat << 'INNEREOF' > build-logic/convention/build.gradle.kts
plugins {
    \`kotlin-dsl\`
}
group = "xyz.alaniz.aaron.lightsaber.buildlogic"
dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.21")
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
INNEREOF

cat << 'INNEREOF' > build-logic/convention/src/main/kotlin/convention/KmpLibraryConventionPlugin.kt
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
INNEREOF

cat << 'INNEREOF' > build-logic/convention/src/main/kotlin/convention/KmpComposeConventionPlugin.kt
package convention
import org.gradle.api.Plugin
import org.gradle.api.Project
class KmpComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
        }
    }
}
INNEREOF

cat << 'INNEREOF' > build-logic/convention/src/main/kotlin/convention/KmpCircuitConventionPlugin.kt
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
INNEREOF

# Setup project structure
cat << 'INNEREOF' > settings.gradle.kts
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
INNEREOF

# Create Modules
mkdir -p core/ui/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/common core/ui/src/commonMain/composeResources/drawable core/ui/src/commonMain/composeResources/values core/ui/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/components core/ui/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/fixtures
mkdir -p core/data/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/data core/data/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro
mkdir -p core/audio/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/audio core/audio/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/audio core/audio/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/audio core/audio/src/commonMain/composeResources/files/raw core/audio/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro core/audio/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro core/audio/src/androidUnitTest/kotlin/xyz/alaniz/aaron/lightsaber/audio core/audio/src/iosTest/kotlin/xyz/alaniz/aaron/lightsaber/audio
mkdir -p core/motion/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/motion core/motion/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/motion core/motion/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/motion core/motion/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro core/motion/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro
mkdir -p core/ui/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/util

mkdir -p feature/lightsaber/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber feature/lightsaber/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber feature/lightsaber/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber feature/lightsaber/src/commonMain/composeResources/drawable feature/lightsaber/src/commonMain/composeResources/values feature/lightsaber/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber
mkdir -p feature/settings/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings feature/settings/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings feature/settings/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings feature/settings/src/commonMain/composeResources/values

# Build Graddles
cat << 'INNEREOF' > core/ui/build.gradle.kts
plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.compose")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.core.ui" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)
                api(libs.compose.colorpicker)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
            }
        }
    }
}
INNEREOF

cat << 'INNEREOF' > core/data/build.gradle.kts
plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.core.data" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.androidx.datastore.core.okio)
                implementation(libs.circuit.foundation)
                api(libs.circuit.codegen.annotations)
            }
        }
    }
}
INNEREOF

cat << 'INNEREOF' > core/audio/build.gradle.kts
plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.core.audio" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                implementation(libs.kermit)
                implementation(libs.compose.resources)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
            }
        }
    }
}
INNEREOF

cat << 'INNEREOF' > core/motion/build.gradle.kts
plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.core.motion" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kermit)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
            }
        }
    }
}
INNEREOF

cat << 'INNEREOF' > feature/lightsaber/build.gradle.kts
plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.compose")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.feature.lightsaber" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                implementation(project(":core:audio"))
                implementation(project(":core:motion"))
                implementation(project(":core:data"))
                api(project(":feature:settings"))
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
            }
        }
    }
}
INNEREOF

cat << 'INNEREOF' > feature/settings/build.gradle.kts
plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.compose")
    id("lightsaber.kmp.circuit")
}
android { namespace = "xyz.alaniz.aaron.lightsaber.feature.settings" }
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                implementation(project(":core:data"))
                implementation(libs.circuitx.gesture.navigation)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
            }
        }
    }
}
INNEREOF

# Move files
mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/common/* core/ui/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/common/ 2>/dev/null || true
mv shared/src/commonMain/composeResources/drawable/compose_multiplatform.xml core/ui/src/commonMain/composeResources/drawable/ 2>/dev/null || true
mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/settings/ColorPickerDialog.kt core/ui/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/components/ 2>/dev/null || true
mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/util/* core/ui/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/util/ 2>/dev/null || true

mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/data/* core/data/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/data/ 2>/dev/null || true
mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/di/DataStorePath.kt core/data/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/di/ 2>/dev/null || true
mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/DatastoreProvider.kt core/data/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/ 2>/dev/null || true

mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/* core/audio/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/ 2>/dev/null || true
mv shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/* core/audio/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/ 2>/dev/null || true
mv shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/* core/audio/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/ 2>/dev/null || true
mv shared/src/commonMain/composeResources/files/raw/*.m4a core/audio/src/commonMain/composeResources/files/raw/ 2>/dev/null || true
mv shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/AndroidSoundProvider.kt core/audio/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/ 2>/dev/null || true
mv shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/IosSoundProvider.kt core/audio/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/ 2>/dev/null || true
mv shared/src/androidUnitTest/kotlin/xyz/alaniz/aaron/lightsaber/audio/* core/audio/src/androidUnitTest/kotlin/xyz/alaniz/aaron/lightsaber/audio/ 2>/dev/null || true
mv shared/src/iosTest/kotlin/xyz/alaniz/aaron/lightsaber/audio/* core/audio/src/iosTest/kotlin/xyz/alaniz/aaron/lightsaber/audio/ 2>/dev/null || true
mv shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/AppContext.kt core/audio/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/ 2>/dev/null || true

mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/* core/motion/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/ 2>/dev/null || true
mv shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/* core/motion/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/ 2>/dev/null || true
mv shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/* core/motion/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/ 2>/dev/null || true
mv shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/AndroidMotionProvider.kt core/motion/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/ 2>/dev/null || true
mv shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/IosMotionProvider.kt core/motion/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/di/metro/ 2>/dev/null || true

mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/lightsaber/* feature/lightsaber/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/ 2>/dev/null || true
mv shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/lightsaber/* feature/lightsaber/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/ 2>/dev/null || true
mv shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/lightsaber/* feature/lightsaber/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/ 2>/dev/null || true
mv shared/src/commonMain/composeResources/drawable/lightsaber_handle.xml feature/lightsaber/src/commonMain/composeResources/drawable/ 2>/dev/null || true
mv shared/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/ui/* feature/lightsaber/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/ 2>/dev/null || true

mv shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/settings/* feature/settings/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings/ 2>/dev/null || true
mv shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/settings/* feature/settings/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings/ 2>/dev/null || true
mv shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/settings/* feature/settings/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings/ 2>/dev/null || true

mv shared/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/fixtures/* core/ui/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/fixtures/ 2>/dev/null || true

# Clean up empty directories in shared
rm -rf shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/common/
rm -rf shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/data/
rm -rf shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/
rm -rf shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/
rm -rf shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/lightsaber/
rm -rf shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/settings/
rm -rf shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/util/
rm -rf shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/
rm -rf shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/
rm -rf shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/lightsaber/
rm -rf shared/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/settings/
rm -rf shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/audio/
rm -rf shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/motion/
rm -rf shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/lightsaber/
rm -rf shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/settings/
rm -rf shared/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/ui/
rm -rf shared/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/fixtures/
rm -rf shared/src/commonTest/kotlin/xyz/alaniz/aaron/lightsaber/util/
rm -rf shared/src/androidUnitTest
rm -rf shared/src/iosTest
rm -rf shared/src/commonMain/composeResources/files/
rm -rf shared/src/commonMain/composeResources/drawable/

cat << 'INNEREOF' > core/ui/src/commonMain/composeResources/values/strings.xml
<resources>
    <string name="color_picker_dialog_title">Select a Color</string>
    <string name="color_picker_dialog_button_ok">OK</string>
    <string name="color_picker_dialog_button_cancel">Cancel</string>
</resources>
INNEREOF

cat << 'INNEREOF' > feature/lightsaber/src/commonMain/composeResources/values/strings.xml
<resources>
    <string name="lightsaber_screen_settings_icon">settings icon</string>
    <string name="lightsaber_screen_lightsaber_blade">lightsaber blade</string>
    <string name="lightsaber_screen_lightsaber_handle">lightsaber handle</string>
</resources>
INNEREOF

cat << 'INNEREOF' > feature/settings/src/commonMain/composeResources/values/strings.xml
<resources>
    <string name="settings_screen_app_bar_title">Settings</string>
    <string name="settings_screen_app_bar_back">back</string>
    <string name="settings_screen_lightsaber_group">Lightsaber</string>
    <string name="settings_screen_lightsaber_blade_color">Blade Color</string>
    <string name="settings_screen_about_group">About</string>
    <string name="settings_screen_version">Version</string>
</resources>
INNEREOF

rm shared/src/commonMain/composeResources/values/strings.xml 2>/dev/null || true

# Update usages in ColorPickerDialog for its new location
sed -i 's/package xyz.alaniz.aaron.lightsaber.ui.settings/package xyz.alaniz.aaron.lightsaber.ui.components/' core/ui/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/components/ColorPickerDialog.kt
sed -i 's/xyz.alaniz.aaron.lightsaber.shared.generated.resources/xyz.alaniz.aaron.lightsaber.core.ui.generated.resources/g' core/ui/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/ui/components/ColorPickerDialog.kt

# Feature settings updates
sed -i 's/package xyz.alaniz.aaron.lightsaber.ui.settings/package xyz.alaniz.aaron.lightsaber.feature.settings/g' feature/settings/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings/*.kt
sed -i 's/import xyz.alaniz.aaron.lightsaber.ui.components.ColorPickerDialog/import xyz.alaniz.aaron.lightsaber.ui.components.ColorPickerDialog/' feature/settings/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings/SettingsUi.kt
sed -i 's/xyz.alaniz.aaron.lightsaber.shared.generated.resources/xyz.alaniz.aaron.lightsaber.feature.settings.generated.resources/g' feature/settings/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings/*.kt
sed -i '1i import xyz.alaniz.aaron.lightsaber.ui.components.ColorPickerDialog' feature/settings/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings/SettingsUi.kt

# Feature lightsaber updates
sed -i 's/package xyz.alaniz.aaron.lightsaber.ui.lightsaber/package xyz.alaniz.aaron.lightsaber.feature.lightsaber/g' feature/lightsaber/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/*.kt
sed -i 's/xyz.alaniz.aaron.lightsaber.shared.generated.resources/xyz.alaniz.aaron.lightsaber.feature.lightsaber.generated.resources/g' feature/lightsaber/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/*.kt
sed -i 's/xyz.alaniz.aaron.lightsaber.ui.settings.SettingsScreen/xyz.alaniz.aaron.lightsaber.feature.settings.SettingsScreen/g' feature/lightsaber/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/*.kt
sed -i 's/xyz.alaniz.aaron.lightsaber.util.noop/xyz.alaniz.aaron.lightsaber.util.noop/g' feature/lightsaber/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/*.kt

# shared setup
cat << 'INNEREOF' > shared/build.gradle.kts
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    id("lightsaber.kmp.library")
    id("lightsaber.kmp.compose")
    id("lightsaber.kmp.circuit")
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.burst)
}

kotlin {
    cocoapods {
        version = "1.0.0"
        summary = "A toy Lightsaber app built with Compose Multiplatform"
        homepage = "https://github.com/aaalaniz/compose-lightsaber"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
            export(project(":core:ui"))
            export(project(":core:data"))
            export(project(":core:audio"))
            export(project(":core:motion"))
            export(project(":feature:lightsaber"))
            export(project(":feature:settings"))
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:ui"))
                api(project(":core:data"))
                api(project(":core:audio"))
                api(project(":core:motion"))
                api(project(":feature:lightsaber"))
                api(project(":feature:settings"))
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.resources)
                api(libs.circuit.codegen.annotations)
                implementation(libs.circuit.foundation)
                implementation(libs.circuitx.gesture.navigation)
                api(libs.kermit)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.compose.ui.test)
            }
        }
        androidMain {
            dependencies {
                api(libs.androidx.activity.compose)
                api(libs.androidx.appcompat)
                api(libs.androidx.core.ktx)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.compose.ui.test.junit4.android)
            }
        }
    }
}

android {
    namespace = "xyz.alaniz.aaron.lightsaber"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
INNEREOF

cat << 'INNEREOF' > feature/settings/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/settings/SettingsScreen.kt
package xyz.alaniz.aaron.lightsaber.feature.settings

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object SettingsScreen : Screen
INNEREOF

cat << 'INNEREOF' > feature/lightsaber/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/feature/lightsaber/LightsaberScreen.kt
package xyz.alaniz.aaron.lightsaber.feature.lightsaber

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object LightsaberScreen : Screen
INNEREOF

# Update App.kt imports
sed -i 's/import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberScreen/import xyz.alaniz.aaron.lightsaber.feature.lightsaber.LightsaberScreen/g' shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/App.kt
sed -i 's/import xyz.alaniz.aaron.lightsaber.ui.settings.SettingsScreen/import xyz.alaniz.aaron.lightsaber.feature.settings.SettingsScreen/g' shared/src/commonMain/kotlin/xyz/alaniz/aaron/lightsaber/App.kt
sed -i 's/xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberScreen/xyz.alaniz.aaron.lightsaber.feature.lightsaber.LightsaberScreen/g' androidApp/src/androidMain/kotlin/xyz/alaniz/aaron/lightsaber/MainActivity.kt
sed -i 's/xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberScreen/xyz.alaniz.aaron.lightsaber.feature.lightsaber.LightsaberScreen/g' shared/src/iosMain/kotlin/xyz/alaniz/aaron/lightsaber/main.ios.kt

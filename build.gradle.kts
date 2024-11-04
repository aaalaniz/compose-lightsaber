plugins {
    /**
     * Avoid plugins be loaded multiple times in each subproject's classloader
     */
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.kotlin.plugin.parcelize).apply(false)
    alias(libs.plugins.kotlin.native.cocoapods).apply(false)
    alias(libs.plugins.burst).apply(false)
}

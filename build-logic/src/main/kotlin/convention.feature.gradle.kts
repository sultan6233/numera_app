import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val libsCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
val androidCompileSdk = libsCatalog.findVersion("android-compileSdk").get().requiredVersion.toInt()
val androidMinSdk = libsCatalog.findVersion("android-minSdk").get().requiredVersion.toInt()

extensions.configure<KotlinMultiplatformExtension> {
    android {
        compileSdk = androidCompileSdk
        minSdk = androidMinSdk
        androidResources.enable = true
    }

    iosArm64()
    iosSimulatorArm64()

    jvmToolchain(17)

    sourceSets {
        commonMain.dependencies {
            implementation(libsCatalog.findLibrary("compose-runtime").get())
            implementation(libsCatalog.findLibrary("compose-foundation").get())
            implementation(libsCatalog.findLibrary("compose-material3").get())
            implementation(libsCatalog.findLibrary("compose-ui").get())
            implementation(libsCatalog.findLibrary("androidx-lifecycle-viewmodel").get())
            implementation(libsCatalog.findLibrary("androidx-lifecycle-viewmodelCompose").get())
            implementation(libsCatalog.findLibrary("androidx-lifecycle-runtimeCompose").get())
            implementation(libsCatalog.findLibrary("androidx-navigation3-ui").get())
            implementation(libsCatalog.findLibrary("compose-components-resources").get())
            implementation(libsCatalog.findLibrary("kotlinx-serialization-core").get())
        }
    }
}

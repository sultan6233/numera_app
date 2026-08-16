plugins {
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.multiplatform")
    alias(libs.plugins.metro)
}

configurations.all {
    resolutionStrategy.eachDependency {
        // com.android.billingclient:billing pulls in old transitive androidx.lifecycle/
        // fragment/loader versions that aren't relevant to this app (it doesn't use
        // Activity/Fragment lifecycle integration) - align them to the version already used
        // elsewhere in the project instead of resolving a second, unrelated old lifecycle graph.
        if (requested.group == "androidx.lifecycle") {
            useVersion(libs.versions.androidx.lifecycle.get())
        }
        if (requested.group == "androidx.core" && requested.name == "core") {
            useVersion(libs.versions.androidx.core.get())
        }
    }
}

kotlin {
    android {
        namespace = "com.numera.app.core.billing"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    jvmToolchain(17)

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
        androidMain.dependencies {
            implementation("com.android.billingclient:billing:8.3.0") {
                // Pulls in an unrelated, heavy transitive graph (activity -> navigationevent ->
                // compose-runtime, fragment, loader...) for KTX activity-result helpers this app
                // doesn't use - AndroidBillingClient talks to the plain callback API directly.
                exclude(group = "androidx.activity")
                exclude(group = "androidx.fragment")
            }
        }
    }
}

plugins {
    id("convention.feature")
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.numera.app.features.profile"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.billing)
            implementation(libs.androidx.core.ktx)
        }
    }
}

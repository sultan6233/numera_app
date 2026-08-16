plugins {
    id("convention.feature")
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.numera.app.features.growth"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(libs.androidx.core.ktx)
        }
    }
}

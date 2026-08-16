plugins {
    id("convention.feature")
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.numera.app.features.onboarding"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.numerology)
            implementation(libs.androidx.core.ktx)
        }
    }
}

plugins {
    id("convention.feature")
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.numera.app.features.numbers"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.numerology)
            implementation(projects.core.billing)
            implementation(libs.androidx.core.ktx)
        }
    }
}

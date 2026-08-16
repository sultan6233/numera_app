plugins {
    id("convention.feature")
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.numera.app.features.compatibility"
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(projects.core.designsystem)
            implementation(projects.core.numerology)
            implementation(libs.androidx.core.ktx)
        }
    }
}

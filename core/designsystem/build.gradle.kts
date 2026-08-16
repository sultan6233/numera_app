plugins {
    id("convention.feature")
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.numera.app.designsystem"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.multiplatform.settings)
            implementation(libs.androidx.core.ktx)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
compose {
    resources {
        publicResClass = true
    }
}

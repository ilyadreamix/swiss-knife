import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.app)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.compose)
}

kotlin {
  jvmToolchain(17)
}

tasks.withType<KotlinCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
  }
}

android {
  compileSdk = libs.versions.app.android.sdk.compile.get().toInt()
  namespace = "io.github.ilyadreamix.swissknife.example"

  defaultConfig {
    applicationId = "io.github.ilyadreamix.swissknife.example"

    minSdk = libs.versions.app.android.sdk.min.get().toInt()
    targetSdk = libs.versions.app.android.sdk.target.get().toInt()

    versionCode = 1
    versionName = "1.0.0"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  buildFeatures {
    compose = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      excludes += "/META-INF/DEPENDENCIES"
    }
  }
}

dependencies {
  implementation(projects.dialogs)

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.core)
  implementation(libs.androidx.appCompat)
  implementation(libs.compose.material3)
  implementation(libs.compose.ui.library)
  implementation(libs.compose.foundation)
  implementation(libs.compose.runtime)
  implementation(libs.compose.ui.tooling.preview)
  debugImplementation(libs.compose.ui.tooling.library)
}

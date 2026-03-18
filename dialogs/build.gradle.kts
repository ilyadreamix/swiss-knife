import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `maven-publish`

  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.compose)
}

kotlin {
  jvmToolchain(17)

  applyDefaultHierarchyTemplate()

  android {
    compileSdk = libs.versions.app.android.sdk.compile.get().toInt()
    minSdk = libs.versions.app.android.sdk.min.get().toInt()

    namespace = "io.github.ilyadreamix.swissknife.dialogs"

    tasks.withType<KotlinCompile>().configureEach {
      compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
      }
    }

    withHostTest { /* ... */ }
    withDeviceTest { /* ... */ }

    androidResources {
      enable = true
    }
  }

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    commonMain.dependencies {
      api(projects.core)

      implementation(libs.compose.ui.library)
      implementation(libs.compose.foundation)
      implementation(libs.compose.runtime)
      implementation(libs.compose.ui.tooling.preview)
    }

    androidMain.dependencies {
      implementation(libs.androidx.core)
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.lifecycle.viewModel)
      implementation(libs.androidx.appCompat)
    }

    dependencies.androidRuntimeClasspath(libs.compose.ui.tooling.library)
  }
}

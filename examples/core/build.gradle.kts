import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
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

    namespace = "io.github.ilyadreamix.swissknife.example.core"

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

  listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
    it.binaries.framework {
      baseName = "SKExampleCore"
      isStatic = true
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(projects.dialogs)

      implementation(libs.compose.material3)
      implementation(libs.compose.ui.library)
      implementation(libs.compose.foundation)
      implementation(libs.compose.runtime)
      implementation(libs.compose.ui.tooling.preview)
    }

    dependencies.androidRuntimeClasspath(libs.compose.ui.tooling.library)
  }
}

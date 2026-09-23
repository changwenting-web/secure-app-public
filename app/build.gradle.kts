import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.secureapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.secureapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    // 可选：release 签名。keystore 密码从 local.properties / 环境变量读取，
    // keystore 文件本身不入库（见 .gitignore），保证签名密钥私有。
    val keystoreFile = rootProject.file("release.keystore")
    signingConfigs {
        if (keystoreFile.exists()) {
            create("release") {
                storeFile = keystoreFile
                val localProps = Properties().apply {
                    val f = rootProject.file("local.properties")
                    if (f.exists()) f.inputStream().use { load(it) }
                }
                storePassword = (localProps.getProperty("RELEASE_STORE_PASSWORD")
                    ?: System.getenv("RELEASE_STORE_PASSWORD"))
                keyAlias = (localProps.getProperty("RELEASE_KEY_ALIAS")
                    ?: System.getenv("RELEASE_KEY_ALIAS") ?: "release")
                keyPassword = (localProps.getProperty("RELEASE_KEY_PASSWORD")
                    ?: System.getenv("RELEASE_KEY_PASSWORD"))
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (keystoreFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
}

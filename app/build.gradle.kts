import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.secureapp"
    compileSdk = 35

    defaultConfig {
        // 公有版使用独立包名，可与私有版（com.example.secureapp）在同一设备共存
        applicationId = "com.example.secureapp.public"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    // 两种 release 签名：
    // 1) 私有密钥（可选）：根目录 release.keystore + local.properties/环境变量中的密码，
    //    keystore 不入库，用于正式发布（存在时优先使用）。
    // 2) 公开密钥（默认）：app/public.keystore，密码公开、随仓库提供，谁都能复现签名，
    //    安全性等同 debug 包，不能用于上架。
    val keystoreFile = rootProject.file("release.keystore")
    signingConfigs {
        create("public") {
            storeFile = file("public.keystore")
            storePassword = "public123"
            keyAlias = "public"
            keyPassword = "public123"
        }
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
            // 优先用私有密钥；否则使用公开密钥，保证产出始终可直接安装
            signingConfig = if (keystoreFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("public")
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

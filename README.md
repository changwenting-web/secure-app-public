# SecureAppAndroid

一个标准的 **Android 原生应用**示例（Kotlin + Gradle Kotlin DSL），可在 Android Studio 中直接打开并打包成 **APK / AAB**。

## 技术栈

| 项 | 版本 |
|----|------|
| Gradle | 8.11.1 |
| Android Gradle Plugin | 8.7.3 |
| Kotlin | 2.0.21 |
| compileSdk / targetSdk | 35 |
| minSdk | 24（Android 7.0） |

## 功能

启动页显示标题与点击计数，点击按钮「点我 +1」实时累加。

## 在 Android Studio 中运行

1. Android Studio → **Open** → 选择本目录
2. 等待 Gradle Sync 完成（首次会自动下载依赖）
3. 选择模拟器或真机，点击 ▶ Run

## 命令行打包

```bash
# Debug APK：app/build/outputs/apk/debug/app-debug.apk
./gradlew assembleDebug

# Release APK（未配置签名时产出未签名包）
./gradlew assembleRelease

# AAB（上架 Google Play 用）：app/build/outputs/bundle/release/app-release.aab
./gradlew bundleRelease
```

## Release 签名（密钥私有）

签名 keystore **不入库**（`*.keystore` / `*.jks` 已在 `.gitignore` 中），配置方式：

1. 生成 keystore 放到项目根目录，命名为 `release.keystore`：
   ```bash
   keytool -genkey -v -keystore release.keystore -alias release \
     -keyalg RSA -keysize 2048 -validity 10000
   ```
2. 在 **`local.properties`**（同样不入库）中写入：
   ```properties
   RELEASE_STORE_PASSWORD=你的库密码
   RELEASE_KEY_ALIAS=release
   RELEASE_KEY_PASSWORD=你的key密码
   ```
   也可改用同名环境变量提供。
3. 再执行 `./gradlew assembleRelease`，即输出已签名 APK。

## 目录结构

```
app/
├── build.gradle.kts
└── src/main/
    ├── AndroidManifest.xml
    ├── java/com/example/secureapp/MainActivity.kt
    └── res/（layout、values、drawable）
```

## 许可证

MIT

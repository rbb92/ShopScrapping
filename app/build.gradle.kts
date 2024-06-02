plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.example.shopscrapping"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shopscrapping"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        allWarningsAsErrors = false
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "mozilla/public-suffix-list.txt"
        }
    }
}

dependencies {
    implementation("com.google.android.engage:engage-core:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    val ktor_version = "2.3.7"
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")

//    implementation("com.google.accompanist:accompanist-coil:0.10.0")
    implementation("io.coil-kt:coil-compose:1.3.2")
    implementation("io.coil-kt:coil:1.3.2")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")

    //Works
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    //Room
    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")


    //Scrap
    implementation("it.skrape:skrapeit:1.1.5"){
        exclude(group = "net.sourceforge.htmlunit", module = "htmlunit")
        exclude(group = "net.sourceforge.htmlunit", module = "htmlunit-android")
        exclude(group = "net.sourceforge.htmlunit", module = "htmlunit-cssparser")
    }
    implementation("it.skrape:skrapeit-browser-fetcher:1.1.5"){
        exclude(group = "net.sourceforge.htmlunit", module = "htmlunit")
        exclude(group = "net.sourceforge.htmlunit", module = "htmlunit-android")
        exclude(group = "net.sourceforge.htmlunit", module = "htmlunit-cssparser")
    }
    implementation("net.sourceforge.htmlunit:htmlunit-android:2.63.0")

    //Json parser
    implementation("com.beust:klaxon:5.5")

    //Spotlight
    implementation("com.github.takusemba:spotlight:2.0.5")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-network:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-android:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-json:$ktor_version")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
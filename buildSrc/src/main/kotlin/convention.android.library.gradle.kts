plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

version = System.getenv("SDK_VERSION_NAME") ?: Library.version

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField(
            "String",
            "SDK_VERSION_NAME",
            "\"$version\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField(
                "Boolean",
                "IS_TIME_TRAVEL",
                "true"
            )
        }

    }

    flavorDimensions.add("mode")
    productFlavors {
        create("nl3") {
            dimension = "mode"
            buildConfigField(
                "String",
                "API_HOST",
                "\"pp-sdk.westresscode.net\""
            )

            buildConfigField(
                "String",
                "WS_API_HOST",
                "\"paymentpage.westresscode.net\""
            )
        }

        create("prod") {
            dimension = "mode"
            buildConfigField(
                "String",
                "API_HOST",
                System.getenv("SDK_API_HOST") ?: "\"sdk.ecommpay.com\""
            )

            buildConfigField(
                "String",
                "WS_API_HOST",
                System.getenv("SDK_WS_API_HOST") ?: "\"paymentpage.ecommpay.com\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        quiet = true
        abortOnError = false
        ignoreWarnings = true
        checkDependencies = true

        enable.add("RtlHardcoded")
        enable.add("RtlCompat")
        enable.add("RtlEnabled")

        disable.add("TypographyFractions")
        disable.add("TypographyQuotes")
    }
}

dependencies {
    //CardIO (card scanning)
    implementation(LibraryDependencies.CardIO.cardIO)
    //AndroidX
    implementation(LibraryDependencies.AndroidX.appCompat)
    //Coil
    implementation(LibraryDependencies.Coil.compose)
    //Google
    implementation(LibraryDependencies.Google.wallet)
    //Compose
    implementation(LibraryDependencies.Compose.ui)
    implementation(LibraryDependencies.Compose.material)
    implementation(LibraryDependencies.Compose.animationCore)
    implementation(LibraryDependencies.Compose.toolingPreview)
    debugImplementation(LibraryDependencies.Compose.uiTooling)
    implementation(LibraryDependencies.Compose.navigation)
    implementation(LibraryDependencies.Compose.activity)
    //Accompanist
    implementation(LibraryDependencies.Accompanist.navigation)
    implementation(LibraryDependencies.Accompanist.permissions)
    //Testing
    testImplementation(LibraryDependencies.Compose.junit4)
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.4")
    testImplementation("androidx.test.espresso:espresso-core:3.5.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-Xjvm-default=all"
    }
}
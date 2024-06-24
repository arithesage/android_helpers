plugins {
    alias(libs.plugins.androidLibrary)
    id ("maven-publish")
}

val libPackage = "me.arithesage.java.android.libs"
val libName = "helpers"
val libVersion = "1.0"

//val libPackageAsPath = libPackage.replace (".", "/") +
//                      ("/" + libName + "/" + libVersion)

//val libDebugFilename = (libName + /*"-" + libVersion +*/ "-debug.aar")
//val libReleseFilename = (libName + /*"-" + libVersion +*/ "-release.aar")

//var local_repo_path = System.getenv("HOME") +
//                      ("/Proyectos/Propios/Android/.m2/repository")

android {
    namespace = (libPackage + "." + libName)
    compileSdk = 34

    defaultConfig {
        minSdk = 19

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

publishing {
    publications {
        create<MavenPublication> ("debug") {
            groupId = libPackage
            artifactId = libName
            version = libVersion

            artifact ("${layout.buildDirectory}/outputs/aar/" + libName + "-debug.aar")
        }

        create<MavenPublication> ("release") {
            groupId = libPackage
            artifactId = libName
            version = libVersion

            artifact ("${layout.buildDirectory}/outputs/aar/" + libName + "-release.aar")
        }
    }
}

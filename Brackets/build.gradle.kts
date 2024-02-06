plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.lollipop.brackets"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
}


// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
afterEvaluate {


//            // Creates a Maven publication called "release".
//            release(MavenPublication) {
//                // Applies the component for the release build variant.
//                from components.release
//
//                        // You can then customize attributes of the publication as shown below.
//                        groupId = 'com.example.MyLibrary'
//                artifactId = 'final'
//                version = '1.0'
//            }
//            // Creates a Maven publication called “debug”.
//            debug(MavenPublication) {
//                // Applies the component for the debug build variant.
//                from components . debug
//
//                        groupId = 'com.example.MyLibrary'
//                artifactId = 'final-debug'
//                version = '1.0'
//            }
//        }
//    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.lollipoppp.brackets"
                artifactId = "core"
                version = "1.2"
                afterEvaluate {
                    artifact(tasks.getByName("bundleReleaseAar"))
                }
//                repository(url: mavenLocal().getUrl())
//                repositories { uri(mavenLocal().url) }
//                from(components["java"])
            }
        }
    }
}

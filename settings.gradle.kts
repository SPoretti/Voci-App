
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Mapbox Maven repository
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1Ijoic3AxMDMiLCJhIjoiY201OGlvNDA5MGpvMTJyc2FrOG05anIxeCJ9.FkYABWAXr2BqXzbPJ7BkrA"
            }
        }
    }
}

rootProject.name = "Voci App"
include(":app")

//allprojects {
//    repositories {
//        maven {
//            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
//            authentication {
//                basic(BasicAuthentication)
//            }
//            credentials {
//                username = "mapbox"
//                password = project.properties["MAPBOX_DOWNLOADS_TOKEN"].toString() ?: ""
//            }
//        }
//    }
//}
 
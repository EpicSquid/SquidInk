pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		maven("https://maven.neoforged.net/releases")
		maven("https://maven.parchmentmc.org")
		maven("https://repo.spongepowered.org/repository/maven-public/")
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id == "org.spongepowered.mixin") {
				useModule("org.spongepowered:mixingradle:0.7-SNAPSHOT")
			}
		}
	}
}

rootProject.name = "SquidInk"

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
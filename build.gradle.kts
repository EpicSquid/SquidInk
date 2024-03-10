import java.text.SimpleDateFormat
import java.util.*

plugins {
	kotlin("jvm").version("1.9.22")
	kotlin("plugin.serialization").version("1.9.22")
	id("eclipse")
	id("idea")
	id("maven-publish")
	id("net.minecraftforge.gradle").version("[6.0,6.2)")
	id("org.parchmentmc.librarian.forgegradle").version("1.+")
	id("org.spongepowered.mixin")
}

val minecraftVersion: String by extra
val minecraftVersionRange: String by extra
val loaderVersionRange: String by extra
val forgeVersionRange: String by extra
val modVersion: String by extra
val modGroupId: String by extra
val modId: String by extra
val modAuthors: String by extra
val modDescription: String by extra
val modLicense: String by extra
val modName: String by extra
val parchmentChannel: String by extra
val parchmentVersion: String by extra
val forgeVersion: String by extra
val jeiVersion: String by extra
val curiosVersion: String by extra
val mixinVersion: String by extra
val modJavaVersion: String by extra
val kotlinVersion: String by extra
val coroutinesVersion: String by extra
val serializationVersion: String by extra
val registrateVersion: String by extra

version = "$minecraftVersion-$modVersion"
if (System.getenv("BUILD_NUMBER") != null) {
	version = "$minecraftVersion-$modVersion.${System.getenv("BUILD_NUMBER")}"
}
group = modGroupId

val baseArchivesName = modId
base {
	archivesName.set(baseArchivesName)
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(modJavaVersion))
	}
	withSourcesJar()
}

jarJar.enable()

mixin {
	add(sourceSets.main.get(), "${modId}.refmap.json")
}

minecraft {
	mappings(parchmentChannel, parchmentVersion)

	accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

	copyIdeResources.set(true)
	// Default run configurations.
	// These can be tweaked, removed, or duplicated as needed.
	runs {
		// applies to all the run configs below
		configureEach {
			workingDirectory(project.file("run"))

			property("forge.logging.markers", "REGISTRIES")
			property("forge.logging.console.level", "debug")

			mods {
				create("${modId}") {
					source(sourceSets.main.get())
				}
			}
		}

		create("client") {
			// Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
			property("forge.enabledGameTestNamespaces", modId)
			arg("-mixin.config=$modId.mixins.json")
		}

		create("server") {
			property("forge.enabledGameTestNamespaces", modId)
			args("--nogui")
			arg("-mixin.config=$modId.mixins.json")
		}

		create("data") {
			// example of overriding the workingDirectory set in configureEach above
			workingDirectory(project.file("run-data"))

			// Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
			args(
				"--mod",
				modId,
				"--all",
				"--output",
				file("src/generated/resources/"),
				"--existing",
				file("src/main/resources/")
			)
		}
	}
}

// Include resources generated by data generators.
sourceSets {
	main {
		resources.srcDir("src/generated/resources")
	}
}

repositories {
	mavenCentral()
	maven {
		name = "Curios maven"
		url = uri("https://maven.theillusivec4.top/")
	}
	maven {
		name = "JEI maven"
		url = uri("https://dvs1.progwml6.com/files/maven")
	}
	maven {
		name = "tterrag maven"
		url = uri("https://maven.tterrag.com/")
	}
	maven {
		name = "BlameJared maven"
		url = uri("https://maven.blamejared.com/")
	}
	maven {
		name = "Curse Maven"
		url = uri("https://cursemaven.com")
		content {
			includeGroup("curse.maven")
		}
	}
}

dependencies {
	minecraft("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")

	if (System.getProperty("idea.sync.active") != "true") {
		annotationProcessor("org.spongepowered:mixin:$mixinVersion:processor")
	}

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
	jarJar("org.jetbrains.kotlin:kotlin-stdlib:[$kotlinVersion, )") {
		jarJar.pin(this, kotlinVersion)
	}
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
	jarJar("org.jetbrains.kotlinx:kotlinx-coroutines-core:[$coroutinesVersion,)") {
		jarJar.pin(this, coroutinesVersion)
	}
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
	jarJar("org.jetbrains.kotlinx:kotlinx-serialization-json:[$serializationVersion,)") {
		jarJar.pin(this, serializationVersion)
	}

	// JEI Dependency
	compileOnly(fg.deobf("mezz.jei:jei-${minecraftVersion}-forge-api:${jeiVersion}"))
	compileOnly(fg.deobf("mezz.jei:jei-${minecraftVersion}-common-api:${jeiVersion}"))
	runtimeOnly(fg.deobf("mezz.jei:jei-${minecraftVersion}-forge:${jeiVersion}"))

	// Curios dependency
	compileOnly(fg.deobf("top.theillusivec4.curios:curios-forge:${curiosVersion}:api"))
	runtimeOnly(fg.deobf("top.theillusivec4.curios:curios-forge:${curiosVersion}"))

	// Registrate
	implementation("com.tterrag.registrate:Registrate:$registrateVersion")
	jarJar("com.tterrag.registrate:Registrate:[$registrateVersion,)") {
		jarJar.pin(this, registrateVersion)
	}
}

tasks.withType<ProcessResources> {
	inputs.property("version", version)

	filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
		expand(
			mapOf(
				"forgeVersionRange" to forgeVersionRange,
				"loaderVersionRange" to loaderVersionRange,
				"minecraftVersion" to minecraftVersion,
				"minecraftVersionRange" to minecraftVersionRange,
				"modAuthors" to modAuthors,
				"modDescription" to modDescription,
				"modId" to modId,
				"modJavaVersion" to modJavaVersion,
				"modName" to modName,
				"modVersion" to version,
				"modLicense" to modLicense,
			)
		)
	}
}

tasks.withType<Jar> {
	val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
	manifest {
		attributes(
			mapOf(
				"Specification-Title" to modName,
				"Specification-Vendor" to modAuthors,
				"Specification-Version" to '1',
				"Implementation-Title" to modName,
				"Implementation-Version" to version,
				"Implementation-Vendor" to modAuthors,
				"Implementation-Timestamp" to now,
			)
		)
	}
	finalizedBy("reobfJar")
}

publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			artifactId = baseArchivesName
			artifact(tasks.jar.get())
		}
	}
	repositories {
		maven {
			url = uri("file://${System.getenv("local_maven")}")
		}
	}
}

idea {
	module {
		for (fileName in listOf("run", "out", "logs")) {
			excludeDirs.add(file(fileName))
		}
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

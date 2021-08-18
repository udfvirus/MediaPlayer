package gradleutils;

import org.gradle.api.Project
import java.io.FileInputStream
import java.util.*

class Keystore(rootProject: Project) {

    private val keystoreProperties = Properties()

    init {
        val keystoreFile = rootProject.file("keystore.properties")
        if (keystoreFile.exists()) {
            keystoreProperties.load(FileInputStream(keystoreFile))
        }
    }

    fun getProperty(key: String): String = when {
        keystoreProperties.containsKey(key) -> keystoreProperties[key] as String
        System.getenv(key) != null -> System.getenv(key)
        else -> throw RuntimeException("Property not found!")
    }
}
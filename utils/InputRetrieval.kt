package utils

import java.io.File
import java.net.URL
import kotlin.io.path.createParentDirectories

object InputRetrieval {
    fun getFile(year: Int, day: Int): File {
        val file = File("aoc$year/inputFiles/day$day.txt")
        // File doesn't exist, download it
        if (!file.exists()) {
            // Create file path, in case it doesn't exist
            file.toPath().createParentDirectories()
            val connection = URL("https://adventofcode.com/$year/day/$day/input").openConnection()
            // Get session token from file
            val sessionCookie = File("auth.txt")
            connection.setRequestProperty("Cookie", sessionCookie.readLines().first())
            // Download the input and save it for offline use
            connection.getInputStream().use {
                file.writeBytes(it.readAllBytes())
            }
        }
        return file
    }
}

package org.example.filecopy

import java.nio.file.Files
import java.nio.file.Paths

class FileNameList {

    fun fileNameAdd(nameListFile: String): MutableList<String> {
        val list = mutableListOf<String>()
        Files.readAllLines(Paths.get(nameListFile)).forEach {
            if (it.toString().contains(".")) {
                list.add(it.substringAfterLast("/"))
            }
        }
        return list
    }
}
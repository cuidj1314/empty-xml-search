package org.work.filecopy

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class OutputFile {

    fun readFileByName(inputPath: String, outputPath: String, fileNameList: MutableList<String>): Int {

        var count = 0
        val fileTree: FileTreeWalk = File(inputPath).walk()
        val fileNameListTemp = mutableListOf<String>()
        fileNameListTemp.addAll(fileNameList)

        fileTree.filter { it.isFile }
            .forEach {
                fileNameList.forEach { x ->
                    run {
                        if (it.name == x) {
                            val endPath = it.path.substringAfterLast(inputPath)
                            this.creatPathAndFile(outputPath + Paths.get(endPath).parent, it)
                            count++
                            fileNameListTemp.remove(it.name)
                        }
                    }
                }
            }
        fileNameListTemp.forEach { y ->
            run {
                println("未出力ファイル：---------> $y")
            }
        }
        return count
    }

    private fun creatPathAndFile(outputPath: String, it: File) {

        Paths.get(outputPath).toFile().mkdirs()
        val ex = Paths.get(outputPath, it.name)
        if (ex.toFile().exists()) {
            ex.toFile().delete()
        }
        println("${it.absolutePath}-->${ex.toFile().absolutePath}")
        Files.copy(it.toPath(), Paths.get(outputPath, it.name))
    }
}
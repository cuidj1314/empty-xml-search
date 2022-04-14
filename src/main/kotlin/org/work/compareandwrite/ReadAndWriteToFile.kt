package org.example

import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import java.io.StringReader
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


const val web_inf = "src\\main\\webapp\\WEB-INF"

const val resource = "src\\main\\resources"

data class FileEncoding(
    val name: String,
    val charset: String
)

open class ReadAndWriteToFile {

    fun mig(before: String, now: String) {
        val beforeList = getCharset(before)
        val tree: FileTreeWalk = Paths.get(now, resource).toFile().walk()
        tree.filter { it.isFile }
            .filter { it.extension == "xml" }
            .filter { !it.nameWithoutExtension.contains("logback") }
            .filter { !it.nameWithoutExtension.contains("mybatis") }
            .filter { !it.nameWithoutExtension.contains("web") }
            .filter { !it.nameWithoutExtension.contains("weblogic") }
            .filter { beforeList.any { x -> x.name == it.nameWithoutExtension } }
            .forEach {
                try {
                    DocumentHelper.parseText(Files.readString(it.toPath(), charset("UTF-8")))
                        .save("shift_jis", it.toPath())
                } catch (ex: Throwable) {
                    println(it.name)
                    println(it.absolutePath)
                }
            }
    }

    private fun getCharset(path: String): List<FileEncoding> {

        val tree: FileTreeWalk = Paths.get(path, web_inf).toFile().walk()
        val charsetEntity: MutableList<FileEncoding> = mutableListOf()
        tree
            .filter { it.isFile }
            .filter { it.extension == "xml" }
            .forEach {
                try {
                    Files.readString(it.toPath(), charset("Shift-jis"))
                    charsetEntity.add(FileEncoding(it.nameWithoutExtension, "Shift-jis"))
                } catch (e: Throwable) {
                    println("異常発生されること！")
                }
            }
        val tree1: FileTreeWalk = Paths.get(path, resource).toFile().walk()
        tree1
            .filter { it.isFile }
            .filter { it.extension == "xml" }
            .filter { !it.nameWithoutExtension.contains("sqlMap") }
            .forEach {
                try {
                    Files.readString(it.toPath(), charset("Shift-jis"))
                    charsetEntity.add(FileEncoding(it.nameWithoutExtension, "Shift-jis"))
                } catch (e: Throwable) {
                    println("異常発生されること！")
                }
            }
        return charsetEntity
    }

    private fun Document.save(encoding: String, path: Path) {
        val format = OutputFormat.createPrettyPrint()
        format.isTrimText = false
        format.encoding = encoding
        val stringWriter = StringWriter()
        val writer = XMLWriter(stringWriter, format)
        writer.isEscapeText = false
        writer.write(this)
        writer.close()
        val xml = stringWriter.toString()
        val stringReader = StringReader(xml)
        val strList = mutableListOf<String>()
        stringReader.readLines().forEach {
            if (it.trim().isNotEmpty())
                strList.add(it)
        }
        Files.write(path, strList, charset(encoding))
    }
}
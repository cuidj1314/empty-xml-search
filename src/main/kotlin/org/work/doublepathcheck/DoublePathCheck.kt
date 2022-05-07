package org.work.doublepathcheck

import org.dom4j.Document
import org.dom4j.DocumentHelper
import java.io.File

open class DoublePathCheck {
    fun doublePathCheck(inputPath: String) {
        val fileTree: FileTreeWalk = File(inputPath).walk()
        fileTree.filter { it.isFile }
            .filter { it.extension == "xml" }
            .filter { it.name.contains("config", ignoreCase = true) }
            .forEach {
                try {
                    val doc: Document = DocumentHelper.parseText(it.readText())
                    if (doc.rootElement.elements().size > 0) {
                        doc.rootElement.elements("action-mappings").forEach { x ->
                            x.elements("action")
                                .forEach { y -> list.add("${it.parent.substringBefore("main")}---------------->${y.attribute("path").value}") }
                        }
                    }
                } catch (e: Throwable) {
                    println("異常ファイル--------------------------->： ${it.path}")
                }
            }
    }
}
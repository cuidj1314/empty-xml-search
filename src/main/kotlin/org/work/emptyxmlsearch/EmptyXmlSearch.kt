package org.work.emptyxmlsearch

import org.dom4j.Document
import org.dom4j.DocumentHelper
import java.io.File

open class EmptyXmlSearch {

    fun emptyXmlSearch(inputPath: String) {
        val fileTree: FileTreeWalk = File(inputPath).walk()
        fileTree.filter { it.isFile }
            .filter { it.extension == "xml" }
            .forEach {
                try {
                    val doc: Document = DocumentHelper.parseText(it.readText())
                    when (doc.rootElement.elements().size) {
                        0 -> println("Emptyファイル名--------------------------->： ${it.path}")
                    }
                } catch (e: Throwable) {
                    println("異常ファイル--------------------------->： ${it.path}")
                }
            }
    }
}
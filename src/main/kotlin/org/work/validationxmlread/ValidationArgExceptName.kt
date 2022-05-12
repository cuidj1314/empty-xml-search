package org.work.validationxmlread

import org.dom4j.Document
import org.dom4j.DocumentHelper
import java.io.File

open class ValidationArgExceptName {

    fun exceptName(inputPath: String) {

        val fileTree: FileTreeWalk = File(inputPath).walk()
        fileTree.filter { it.isFile }
            .filter { it.extension == "xml" }
            .filter { it.name.contains("validation", ignoreCase = true) }
            .forEach {
                try {
                    val doc: Document = DocumentHelper.parseText(it.readText())
                    if (doc.rootElement.elements().size > 0) {
                        doc.rootElement.elements("formset").forEach { x ->
                            x.elements("form").forEach { y ->
                                y.elements("field").forEach { z ->
                                    z.elements("arg").forEach { k ->
                                        run {
                                            if (k.attribute("name") == null) {
                                                map.put(
                                                    "${it.path} ${k.parent.parent.attribute("name").value}",
                                                    k.parent.parent.attribute("name").value
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Throwable) {
                    println("異常ファイル--------------------------->： ${it.path}")
                }
            }
    }

}
package org.example.validatecheck

import org.dom4j.Document
import org.dom4j.DocumentHelper
import java.io.File

class ValidateCheck {

    fun actionPathOutput(inputPath: String) {
        val fileTree: FileTreeWalk = File(inputPath).walk()
        fileTree.filter { it.isFile }
            .filter { it.extension == "xml" }
            .filter { it.name.toString().contains("struts", ignoreCase = true) }
            .forEach {
                try {
                    val doc: Document = DocumentHelper.parseText(it.readText())
                    if (doc.rootElement.elements().size > 0) {
                        doc.rootElement.elements("action-mappings").forEach { x ->
                            x.elements("action").forEach { y ->
                                if (y.attribute("validate") != null) {
                                    if (!y.attribute("path").value.contains("Dispatch", ignoreCase = true)) {
                                        when (y.attribute("validate").value) {
                                            "true" -> {
                                                println(
                                                    "validate必要--------------------------->： ${
                                                        y.attribute("path").value.substringAfter(
                                                            "/"
                                                        )
                                                            .replaceFirstChar { m -> if (m.isLowerCase()) m.uppercase() else m.toString() }
                                                    }Controller.java"
                                                )
                                            }
                                            "false" -> {
                                                println(
                                                    "validate必要はない--------------------------->： ${
                                                        y.attribute("path").value.substringAfter(
                                                            "/"
                                                        )
                                                            .replaceFirstChar { m -> if (m.isLowerCase()) m.uppercase() else m.toString() }
                                                    }Controller.java"
                                                )
                                            }
                                            else -> {
                                                println(
                                                    "確認必要--------------------------->： ${
                                                        y.attribute("path").value.substringAfter(
                                                            "/"
                                                        )
                                                            .replaceFirstChar { m -> if (m.isLowerCase()) m.uppercase() else m.toString() }
                                                    }Controller.java"
                                                )
                                            }
                                        }
                                    } else {
                                        when (y.attribute("validate").value) {
                                            "true" -> {
                                                y.elements("forward").forEach { z ->
                                                    println(
                                                        "validate必要--------------------------->： ${
                                                            z.attribute("path").value.substringAfter(
                                                                "/"
                                                            ).substringBefore(".do")
                                                                .replaceFirstChar { m -> if (m.isLowerCase()) m.uppercase() else m.toString() }
                                                        }Controller.java"
                                                    )
                                                }
                                            }
                                            "false" -> {
                                                y.elements("forward").forEach { z ->
                                                    println(
                                                        "validate必要はない--------------------------->： ${
                                                            z.attribute("path").value.substringAfter(
                                                                "/"
                                                            ).substringBefore(".do")
                                                                .replaceFirstChar { m -> if (m.isLowerCase()) m.uppercase() else m.toString() }
                                                        }Controller.java"
                                                    )
                                                }
                                            }
                                            else -> {

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
package org.work.formget

import org.dom4j.Document
import org.dom4j.DocumentHelper
import java.io.File

open class GetFormName {

    fun formNameGet(before: String, now: String) {
        val fileTree: FileTreeWalk = File(before).walk()
        fileTree.filter { it.isFile }
            .filter { it.extension == "xml" }
            .filter { it.name.contains("struts", ignoreCase = true) }
            .forEach {
                try {
                    // ファイルを読み
                    val doc: Document = DocumentHelper.parseText(it.readText())
                    val rootElement = doc.rootElement
                    if (rootElement.elements().size > 0) {
                        rootElement.elements("form-beans").forEach { x ->
                            run {
                                x.elements().forEach { y ->
                                    run {
                                        if (null != y.attribute("name")) map.put(y.attribute("name").value, it.path)
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Throwable) {
                    println("異常ファイル:${it.path}")
                }
            }
        map.forEach {
            classPathGet(it.key, now + it.value.substringBefore("Java").substringAfter("src"))
        }
        // 結果を出力する
        resultList.distinct().forEach { println(it) }
    }

    /**
     *変換後のパス編集して、リストに追加する
     */
    private fun classPathGet(value: String, now: String) {
        val fileTree: FileTreeWalk = File(now).walk()

        val nameTemp = if (value.startsWith("_")) value.replace("_", "") else value
        fileTree.filter { it.isFile }
            .filter { it.extension == "java" }
            .filter { it.name.contains(nameTemp, ignoreCase = true) }
            .forEach {
                resultList.add(it.path.substringAfter("01_ツール変換後ソース"))
            }

    }
}
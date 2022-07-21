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
                                        if (null != y.attribute("type")) {
                                            if (y.attribute("type").value.substringAfterLast(".") == "DynaValidatorActionFormEx") {
                                                if (null != y.attribute("name")) map.put(
                                                    it.path.substringBeforeLast("src") + "★" + y.attribute(
                                                        "name"
                                                    ).value, it.path + "〇" + it.path
                                                )
                                            } else {
                                                map.put(
                                                    it.path.substringBeforeLast("src") + "★" + y.attribute("type").value.substringAfterLast(
                                                        "."
                                                    ), it.path + "〇" + it.path
                                                )
                                            }
                                        }
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
            classPathGet(
                File(it.value.substringBefore("〇")).parent,
                File(it.value.substringBefore("〇")).name,
                it.key.substringAfter("★"),
//                now + it.value.substringAfter("〇").substringBefore("Java").substringAfter("src")
                now + it.value.substringAfter("〇").substringBeforeLast("src").substringAfterLast("src")
            )
        }
        // 結果を出力する
        resultList.distinct().forEach { println(it) }
    }

    /**
     *変換後のパス編集して、リストに追加する
     */
    private fun classPathGet(xmlPath: String, xmlName: String, value: String, nowPath: String) {
        val fileTree: FileTreeWalk = File(nowPath).walk()

        val nameTemp = if (value.startsWith("_")) value.replace("_", "") else value
        fileTree.filter { it.isFile }
            .filter { it.extension == "java" }
            .filter { it.name.contains(nameTemp, ignoreCase = true) }
            .forEach {
                resultList.add(
                    xmlPath.substringAfter("20_原本ソース一式\\H\\src") + "\t" + xmlName + "\t" + File(it.path.substringAfter("01_ツール変換後ソース")).parent + "\t" + File(
                        it.path.substringAfter("01_ツール変換後ソース")
                    ).name
                )
            }

    }
}
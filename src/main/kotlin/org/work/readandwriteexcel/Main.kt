package org.work.readandwriteexcel

import org.work.readandwriteexcel.exceljoho.Report
import org.work.readandwriteexcel.api.KtExcel
import org.work.readandwriteexcel.api.get
import org.work.readandwriteexcel.api.set
import org.work.readandwriteexcel.api.toStr
import java.io.File

fun main() {
    println("loading..............................")
    val path = "D:\\test\\report"
    val fileNames: MutableList<String> = mutableListOf()
    val fileTree: FileTreeWalk = File(path).walk()
    fileTree
        .filter { it.isFile }
        .filter { it.extension == "xlsx" }
        .forEach {
            fileNames.add(it.absolutePath)
        }
    val list = mutableListOf<Report>()
    fileNames.forEach {
        list.addAll(getList(it))
    }
    println("loading........................over")
    val log = "D:\\test\\log\\ログ一覧.xlsm"
    KtExcel.open(log).use { workbook ->
        val sheet = workbook["ログ一覧"]
        for (i in 3..sheet.lastRowNum + 1) {
            val logId = sheet["E$i"].toStr()
            val roots = list.filter { it.rootId == logId }
            if (roots.isNotEmpty()) {
                sheet["G$i"] = "○"
            }
            KtExcel.write(workbook, "D:\\test\\log\\ログ一覧１.xlsm")
        }
    }
}

fun getList(path: String): MutableList<Report> {
    val list = mutableListOf<Report>()
    KtExcel.open(path).use { workbook ->
        val sheet = workbook[0]
        for (i in 2..sheet.lastRowNum + 1) {
            list.add(
                Report(
                    sheet["A$i"].toStr(),
                    sheet["B$i"].toStr(),
                    sheet["C$i"].toStr(),
                    sheet["D$i"].toStr(),
                    sheet["E$i"].toStr(),
                    sheet["F$i"].toStr(),
                    sheet["G$i"].toStr()
                )
            )
        }
    }
    return list
}







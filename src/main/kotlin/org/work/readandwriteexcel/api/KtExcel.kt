package org.work.readandwriteexcel.api

import org.apache.poi.ss.usermodel.*
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.regex.Pattern

class KtExcel {
    companion object {
        @JvmStatic
        fun open(fileName: String): Workbook {
            return WorkbookFactory.create(FileInputStream(Paths.get(fileName).toFile()))
        }

        @JvmStatic
        fun write(workbook: Workbook, fileName: String) {
            val outputPath = Paths.get(fileName)
            try {
                Files.newOutputStream(outputPath).use {
                    workbook.write(it)
                }
            } catch (e: IOException) {
                throw e
            }
        }

        @JvmStatic
        fun cellIndexToCellLabel(x: Int, y: Int): String {
            require(x >= 0, { "xは0以上でなければなりません" })
            require(y >= 0, { "yは0以上でなければなりません" })
            val cellName = dec26(x, 0)
            return cellName + (y + 1)
        }

        @JvmStatic
        private fun dec26(num: Int, first: Int): String {
            return if (num > 25) {
                dec26(num / 26, 1)
            } else {
                ""
            } + ('A' + (num - first) % 26)
        }
    }
}

operator fun Workbook.get(n: Int): Sheet {
    return this.getSheetAt(n)
}

operator fun Workbook.get(name: String): Sheet {
    return this.getSheet(name)
}

operator fun Sheet.get(n: Int): Row {
    return getRow(n) ?: createRow(n)
}

operator fun Row.get(n: Int): Cell {
    return getCell(n) ?: createCell(n, CellType.BLANK)
}

operator fun Sheet.get(x: Int, y: Int): Cell {
    val row = this[y]
    return row[x]
}

private val RADIX = 26

operator fun Sheet.get(cellLabel: String): Cell {
    val p1 = Pattern.compile("([a-zA-Z]+)([0-9]+)")
    val matcher = p1.matcher(cellLabel)
    matcher.find()

    var num = 0
    matcher.group(1).toUpperCase().reversed().forEachIndexed { i, c ->
        val delta = c.toInt() - 'A'.toInt() + 1
        num += delta * Math.pow(RADIX.toDouble(), i.toDouble()).toInt()
    }
    num -= 1
    return this[num, matcher.group(2).toInt() - 1]
}

fun Cell.toStr(): String = CellProxy(this).toStr()

fun Cell.toInt(): Int = CellProxy(this).toInt()

fun Cell.toDouble(): Double = CellProxy(this).toDouble()

fun Cell.toBoolean(): Boolean = CellProxy(this).toBoolean()

fun Cell.toDate(): Date = CellProxy(this).toDate()

operator fun Sheet.set(cellLabel: String, value: Any) {
    this[cellLabel].setValue(value)
}

operator fun Sheet.set(x: Int, y: Int, value: Any) {
    this[x, y].setValue(value)
}

private fun Cell.setValue(value: Any) {
    when (value) {
        is String -> setCellValue(value)
        is Int -> setCellValue(value.toDouble())
        is Double -> setCellValue(value)
        is Boolean -> setCellValue(value)
        is Date -> {
            // 日付セルはフォーマットしてあげないと日付型にならない
            setCellValue(value)
            val wb = sheet.workbook
            val createHelper = wb.creationHelper
            val cellStyle = wb.createCellStyle()
            val style = createHelper.createDataFormat().getFormat("yyyy/m/d")
            cellStyle.dataFormat = style
            setCellStyle(cellStyle)
        }
        else -> throw IllegalArgumentException("String、Int、Double、Boolean、Dateのみ対応しています")
    }
}
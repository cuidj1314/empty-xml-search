package org.work.readandwriteexcel.api
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.CellValue
import org.apache.poi.ss.usermodel.DateUtil
import java.util.*

class CellProxy(private val cell: Cell) {
    private var cellValue: CellValue? = null

    init {
        if (cell.cellType == CellType.FORMULA) {
            cellValue = getFomulaCellValue(cell)
        }
    }

    private fun getCellTypeEnum(): CellType {
        return if (cellValue == null) {
            cell.cellType
        } else {
            (cellValue as CellValue).cellType
        }
    }

    private fun getStringCellValue(): String {
        return if (cellValue == null) cell.stringCellValue else (cellValue as CellValue).stringValue
    }

    private fun getNumericCellValue(): Double {
        return if (cellValue == null) cell.numericCellValue else (cellValue as CellValue).numberValue
    }

    private fun getBooleanCellValue(): Boolean {
        return if (cellValue == null) cell.booleanCellValue else (cellValue as CellValue).booleanValue
    }

    private fun isDateType(): Boolean {
        return if (cellValue == null) {
            if (cell.cellType == CellType.NUMERIC) DateUtil.isCellDateFormatted(cell)
            else false
        } else {
            if ((cellValue as CellValue).cellType == CellType.NUMERIC) DateUtil.isCellDateFormatted(cell)
            else false
        }
    }

    private fun normalizeNumericString(numeric: Double): String {
        // 44.0のような数値を44として取得するために、入力された数値と小数点以下を切り捨てた数値が
        // 一致した場合には、intにキャストして、小数点以下が表示されないようにしている
        return if (numeric == Math.ceil(numeric)) {
            numeric.toInt().toString()
        } else numeric.toString()
    }

    private fun stringToInt(value: String): Int {
        try {
            return java.lang.Double.parseDouble(value).toInt()
        } catch (e: NumberFormatException) {
            throw IllegalAccessException("cellはintに変換できません")
        }
    }

    private fun stringToDouble(value: String): Double {
        try {
            return java.lang.Double.parseDouble(value)
        } catch (e: NumberFormatException) {
            throw IllegalAccessException("cellはdoubleに変換できません")
        }
    }

    private fun getFomulaCellValue(cell: Cell): CellValue {
        val wb = cell.sheet.workbook
        val helper = wb.creationHelper
        val evaluator = helper.createFormulaEvaluator()
        return evaluator.evaluate(cell)
    }

    fun toStr(): String {
        when (getCellTypeEnum()) {
            CellType.STRING -> return getStringCellValue()
            CellType.NUMERIC -> return if (isDateType()) {
                throw UnsupportedOperationException("今はサポート外")
            } else {
                normalizeNumericString(getNumericCellValue())
            }
            CellType.BOOLEAN -> return getBooleanCellValue().toString()
            CellType.BLANK -> return ""
            else // _NONE, ERROR
            -> throw IllegalAccessException("cellはStringに変換できません")
        }
    }

    fun toInt(): Int {
        when (getCellTypeEnum()) {
            CellType.STRING -> return stringToInt(getStringCellValue())
            CellType.NUMERIC -> return if (isDateType()) {
                throw IllegalAccessException("cellはIntに変換できません")
            } else {
                getNumericCellValue().toInt()
            }
            else -> throw IllegalAccessException("cellはIntに変換できません")
        }
    }

    fun toDouble(): Double {
        when (getCellTypeEnum()) {
            CellType.STRING -> return stringToDouble(getStringCellValue())
            CellType.NUMERIC -> return if (isDateType()) {
                throw IllegalAccessException("cellはDoubleに変換できません")
            } else {
                getNumericCellValue()
            }
            else -> throw IllegalAccessException("cellはDoubleに変換できません")
        }
    }

    fun toBoolean(): Boolean {
        when (getCellTypeEnum()) {
            CellType.BOOLEAN -> return getBooleanCellValue()
            else -> throw IllegalAccessException("cellはBooleanに変換できません")
        }
    }

    fun toDate(): Date {
        when {
            isDateType() -> return cell.dateCellValue
            else -> throw IllegalAccessException("cellはDateに変換できません")
        }
    }
}
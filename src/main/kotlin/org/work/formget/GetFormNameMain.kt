package org.work.formget

const val before = "D:\\svn\\01_基線庫\\"
const val now = "D:\\svn\\03_開発庫\\06_本変換\\ソース\\01_ツール変換後ソース"

fun main(args: Array<String>) {
    val getFormName = GetFormName()
    getFormName.formNameGet(before, now)
}
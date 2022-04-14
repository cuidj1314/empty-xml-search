package org.example

fun main(args: Array<String>) {
    val readFile = ReadFile();
    val validateCheck = ValidateCheck();
//    readFile.emptyXmlSearch("D:\\svn\\03_開発庫\\06_本変換\\ソース\\01_ツール変換後ソース")
//    readFile.emptyXmlSearch("D:\\svn\\03_開発庫\\06_本変換\\ソース\\02_手修正後ソース")
    validateCheck.actionPathOutput("D:\\svn\\01_基線庫\\20211227\\20_原本ソース一式\\H\\src\\KJN\\Java\\C0TjApp")
}


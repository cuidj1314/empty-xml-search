package org.work.validationxmlread

import org.work.doublepathcheck.DoublePathCheck

fun main(args: Array<String>) {

//    val inputPath = "C:\\Users\\cuidj\\Desktop\\test"
    val inputPath = "H\\src"
    val exceptName = ValidationArgExceptName();
    exceptName.exceptName(inputPath)
    for (m in map.entries) {
        println(m.key)
    }
}
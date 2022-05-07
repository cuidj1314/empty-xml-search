package org.work.doublepathcheck

import org.work.emptyxmlsearch.EmptyXmlSearch

fun main(args: Array<String>) {
    val readFile = DoublePathCheck();
//    readFile.doublePathCheck("C:\\Users\\cuidj\\Desktop\\test")
    readFile.doublePathCheck("D:\\svn\\01_基線庫\\20211227\\20_原本ソース一式\\H\\src")
//    list.stream().collect(Collection.groupByTo(Function.i))
    val sameMap = mutableMapOf<String, Int>()
    for (item in list) {
        with(sameMap[item]) {
            if (this == null) {
                sameMap.put(item, 1)
            } else {
                sameMap.put(item, this + 1)
            }

        }
    }
    for (item in sameMap.entries) {
        if (item.value > 1) {
            println("同じパス:${item.key}、出力箇所:${item.value}")
        }
    }
}
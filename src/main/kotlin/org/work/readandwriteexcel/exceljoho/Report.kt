package org.work.readandwriteexcel.exceljoho

data class Report(
    val no: String,
    val oldPath: String,
    val oldFile: String,
    val newPath: String,
    val newFile: String,
    val rootId: String,
    val count: String,
)
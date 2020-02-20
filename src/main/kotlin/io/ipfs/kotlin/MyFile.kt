package io.ipfs.kotlin

import java.io.File
import java.io.InputStream

fun byteArrayOfFilePath(fil_p: String): ByteArray {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val file = File(fil_p)
    val result:ByteArray = file.readBytes()
    
    exiting(here)
    return result
}

fun inputStreamOfFilePath(fil_p: String): InputStream {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val file = File(fil_p)
    val result:InputStream = file.inputStream()
    
    exiting(here)
    return result
}

fun provideAnyFileNameOfWhat(what: String): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input what '$what'")
    val whatLc = what.toLowerCase()
    var anyFileName =
      when (whatLc) {
        "lexeme" -> "test.lex"
	"block" -> what+".yml"
	"yml" -> "test.yml"
        else -> what+".txt"
      }
    println("$here: enter file name for '$what'. Default '$anyFileName'")
    val any_f = standardInputRead()
    if (! (any_f.isNullOrBlank() || any_f.equals("null"))) {
        anyFileName = any_f
    }
    println("$here: input File name is '$anyFileName'")

    exiting(here)
    return anyFileName
}

fun standardInputRead(): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)
	
    val str = readLine().toString()
    
    exiting(here)
    return str
}

fun stringListOfFilePath(fil_p: String): List<String> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val file = File(fil_p)
    val bufferedReader = file.bufferedReader()
    val result:List<String> = bufferedReader.readLines()
    
    exiting(here)
    return result
}

fun stringReadOfFilePath(fil_p: String): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val file = File(fil_p)
    val result: String = file.readText() 
    
    exiting(here)
    return result
}


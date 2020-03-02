package io.ipfs.kotlin

import java.io.File
import io.ipfs.kotlin.defaults.*

/**
 * What       : The different Types of MultiHash 
 * Definition : A MultiHashType describes the result of a Hash Function
 * Definition : MultiHash is a protocol for differentiating well-established cryptographic hash functions
 * Property   : MultiHash addresses size and encoding considerations.
 * Property   : MultiHash is completely defined by the name of its function.
 * Definition : Here a MultiHash characterizes the result of MultiHash protocol.
 * Definition : MultiHashSha 
 * Definition : MultiHashBlake
 * Definition : MultiHashFnCode : digits 1 and two 
 * Definition : MultiHashFnLength : digits 3 and 4 
 * Format     : <varint hash function code><varint digest size in bytes><hash function output>
 * fn code  dig size hash digest
 * -------- -------- ------------------------------------
 * 00010001 00000100 101101100 11111000 01011100 10110101
 * sha1     4 bytes  4 byte sha1 digest
 * Example : MultiHashTypeQm(QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa)
 * Author : Emile Achadde 23 février 2020 at 09:33:04+01:00
 * Revision : parametrized by hash string by Emile Achadde 29 février 2020 at 10:44:35+01:00
 */

sealed class MultiHashType () {
    data class MultiHashTypeShaQm (val hash:String) : MultiHashType()
    data class MultiHashTypeShaZ2 (val hash:String) : MultiHashType()
    data class MultiHashTypeBlake2b (val hash:String) : MultiHashType()

    fun hashOf(): String {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	val result = when (this) {
	    is MultiHashType.MultiHashTypeShaQm -> this.hash
	    is MultiHashType.MultiHashTypeShaZ2 -> this.hash
	    is MultiHashType.MultiHashTypeBlake2b -> this.hash
	}
	
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }
    
    fun stringOf(): String {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	val result = when (this) {
	    is MultiHashType.MultiHashTypeShaQm -> "MultiHashTypeShaQm("+this.hash+")"
	    is MultiHashType.MultiHashTypeShaZ2 -> "MultiHashTypeShaZ2("+this.hash+")"
	    is MultiHashType.MultiHashTypeBlake2b -> "MultiHashTypeBlake2b("+this.hash+")"
	}
	
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }
    
    fun printHashOf(): String {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	val result = hashOf()
	println (result)
	
	exiting(here)
	return result
    }
    
    fun printOf(): String {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	val result = stringOf()
	println (result)
	
	exiting(here)
	return result
    }
    
    companion object {
	fun make (hash: String): MultiHashType {
	    val (here, caller) = moduleHereAndCaller()
	    entering(here, caller)
	    
	    if (isTrace(here)) println("$here: input hash '$hash'")
	    
	    val hash_2 = hash.substring(0, 2)
	    val result = when (hash_2) {
		"Qm" -> MultiHashType.MultiHashTypeShaQm(hash)
		"Z2" -> MultiHashType.MultiHashTypeShaZ2(hash)
		"2b" -> MultiHashType.MultiHashTypeBlake2b(hash)
		else -> {
		    fatalErrorPrint("hash started with 'Qm' 'Z2' or 2b", "'$hash'", "Check", here)
		}
	    }
	    
	    if(isTrace(here)) println ("$here: output result '$result'")
	    
	    exiting(here)
	    return result
	}
    } // companion

} // class

fun multiHashTypeListOfDirectoryPath (dirPat: String): List<MultiHashType> {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    if(isTrace(here)) println ("$here: input dirPat '$dirPat'")
    
    val filTyp = File(dirPat)
    val namHas_l = LocalIpfs().add.directory(filTyp)
    val result = namHas_l.map {h -> MultiHashType.make(h.Hash)}
    if(isTrace(here)) println ("$here: output result '$result'")

    exiting(here)
    return result
}

fun multiHashTypeOfFilePath (filPat: String): MultiHashType {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input filPat '$filPat'")

    val filTyp = File(filPat)
    val strH = LocalIpfs().add.file(filTyp).Hash
    val result = MultiHashType.make (strH)
    if(isTrace(here)) println ("$here: output result '$result'")

    exiting(here)
    return result
}

fun multiHashTypeOfString (str: String): MultiHashType {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input str '$str'")

    val filCon = // file path case
	if (isFilePathOfWord(str)) {
	    stringReadOfFilePath(str)
	}
    else {
	str
    }
	
    val strH = LocalIpfs().add.string(filCon).Hash
    val result = MultiHashType.make (strH)
    if(isTrace(here)) println ("$here: output result '$result'")

    exiting(here)
    return result
}

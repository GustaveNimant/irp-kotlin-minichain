package io.ipfs.kotlin

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
 * Author : Emile Achadde 23 février 2020 at 09:33:04+01:00
 */

sealed class MultiHashType () {
    sealed class MultiHashTypeSha () : MultiHashType () {
	object MultiHashTypeQm : MultiHashTypeSha()
	object MultiHashTypeZ2 : MultiHashTypeSha()
    }
    
    sealed class MultiHashTypeBlake () : MultiHashType () {
	object MultiHashType2b : MultiHashTypeBlake()
    }
    
    override fun toString(): String {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	val result = when (this) {
	    is MultiHashType.MultiHashTypeSha.MultiHashTypeQm -> "MultiHashTypeQm"
	    is MultiHashType.MultiHashTypeSha.MultiHashTypeZ2 -> "MultiHashTypeZ2"
	    is MultiHashType.MultiHashTypeBlake.MultiHashType2b ->"MultiHashType2b"
	}
	
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }
    
    companion object {

	fun make (str: String): MultiHashType {
	    val (here, caller) = hereAndCaller()
	    entering(here, caller)
	    
	    if (isTrace(here)) println("$here: input str '$str'")
	    
	    val str_2 = str.substring(0, 2)
	    val result = when (str_2) {
		"Qm" -> MultiHashType.MultiHashTypeSha.MultiHashTypeQm
		"Z2" -> MultiHashType.MultiHashTypeSha.MultiHashTypeZ2
		"2b" -> MultiHashType.MultiHashTypeBlake.MultiHashType2b
		else -> {
		    fatalErrorPrint("hash starts with 'Qm' or 'Z2'", str, "Check", here)
		}
	    }
	    
	    if(isTrace(here)) println ("$here: output result '$result'")
	    
	    exiting(here)
	    return result
	}
	
    }
}

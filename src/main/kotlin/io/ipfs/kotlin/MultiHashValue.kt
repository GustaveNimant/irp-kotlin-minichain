package io.ipfs.kotlin

/**
 * What is it : The Value of a MultiHash is a Hash string defined by a triplet 
 * fn code  dig size hash digest
 * -------- -------- ------------------------------------
 * 00010001 00000100 101101100 11111000 01011100 10110101
 * sha1     4 bytes  4 byte sha1 digest
 * Author : Emile Achadde 23 février 2020 at 11:36:24+01:00
 */

data class MultiHashValue (val functionCode: Int, val lengthCode: Int, val digestHash: String)

    fun isQmMultiHash (strHas: String): Boolean {
	val result = strHas.substring(0,2) == "Qm"
	return result
	}

    fun isZ2MultiHash (strHas: String): Boolean {
	val result = strHas.substring(0,2) == "z2"
	return result
    }
    


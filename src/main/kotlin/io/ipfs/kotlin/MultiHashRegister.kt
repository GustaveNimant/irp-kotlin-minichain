package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*

/**
 * 
 *
 *
 */

class MultiHashRegister {
    
    var register : MutableMap<String, MultiHashType> = mutableMapOf<String, MultiHashType>()

fun isEmpty (): Boolean {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    val result = register.isEmpty()
    
    if(isTrace(here)) println ("$here: output result $result")
    
    return result
}

fun store (path: String, mulH: MultiHashType) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    if(isTrace(here)) println ("$here: input path '$path'")
    if(isTrace(here)) println ("$here: input mulH '$mulH'")
    
    if (isStored(path)) {
	val value = retrieve(path)
	if (value != mulH) {
	    fatalErrorPrint("MultiHashType already stored for path '$path' were equal to new one", mulH.toString(), "Check", here)
		}
    }
    else {
	register.put(path, mulH)
    }
    if(isTrace(here)) println ("$here: mulH couple has been stored")
}

fun isStored (path: String): Boolean {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    if(isTrace(here)) println ("$here: input path '$path'")

    val mulH = register.get(path)
    val result = when (mulH) {
	is MultiHashType -> register.contains(path) 
	else -> false
    }

    if(isTrace(here)) println ("$here: output result '$result'")

    exiting(here)
    return result
}

fun retrieve (path: String): MultiHashType {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    val mulH = register.get(path)
    val result = when (mulH) {
	is MultiHashType -> mulH 
	else -> {fatalErrorPrint ("", "", "", here)}
    }
    if(isTrace(here)) println ("$here: output result '$result'")
    
    exiting(here)
    return result
}
}

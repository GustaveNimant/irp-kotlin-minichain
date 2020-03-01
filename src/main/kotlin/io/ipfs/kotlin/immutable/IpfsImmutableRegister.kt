package io.ipfs.kotlin.immutable

import io.ipfs.kotlin.*

import io.ipfs.kotlin.defaults.*

/**
 * Storage   : IpfsImmutableValue (content of an Immutable file) by its ImmutableType
 * Needs     : IpfsImmutableType (MultiHashType (strHas)) where strHas is the hash string
 * Needed by : 
 * Author : Emile Achadde 01 mars 2020 at 10:30:45+01:00
 */

class IpfsImmutableRegister {
    
    var register : MutableMap<IpfsImmutableType, IpfsImmutableValue> = mutableMapOf<IpfsImmutableType, IpfsImmutableValue>()

    fun isEmpty (): Boolean {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	val result = register.isEmpty()
	
	if(isTrace(here)) println ("$here: output result $result")
	
	return result
    }

    fun store (immTyp: IpfsImmutableType, immVal: IpfsImmutableValue) {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println ("$here: input immTyp '$immTyp'")
	if(isTrace(here)) println ("$here: input immVal '$immVal'")
	
	if (isStored(immTyp)) {
	    val con = retrieve(immTyp)
	    if (con != immVal) {
		fatalErrorPrint("IpfsImmutableValue already stored for immTyp '$immTyp' were equal to new one", immVal.toString(), "Check", here)
	    }
	}
	else {
	    register.put(immTyp, immVal)
	}
	if(isTrace(here)) println ("$here: immVal couple has been stored")
    }
    
    fun isStored (immTyp: IpfsImmutableType): Boolean {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println ("$here: input immTyp '$immTyp'")
	
	val immVal = register.get(immTyp)
	val result = when (immVal) {
	    is IpfsImmutableValue -> register.contains(immTyp) 
	    else -> false
	}
	
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }
    
    fun retrieve (immTyp: IpfsImmutableType): IpfsImmutableValue {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	val immVal = register.get(immTyp)
	val result = when (immVal) {
	    is IpfsImmutableValue -> immVal 
	    else -> {fatalErrorPrint ("", "", "", here)}
	}
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }
}

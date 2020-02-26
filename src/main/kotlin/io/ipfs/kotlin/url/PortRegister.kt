package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import java.io.File
import java.util.Stack

/**
 * What is it : the Register storing a Port knowing its Type.
 * Example : (PortWebui, "5001") 
 * What to do : provide host and port by asking if stored in ParameterMap 
 * Author : Emile Achadde 25 février 2020 at 19:03:02+01:00
 */

class PortRegister {

    var register : MutableMap<PortType, PortValue> = mutableMapOf<PortType, PortValue>()
	 
    fun isEmpty (): Boolean {
	val (here, caller) = hereAndCaller()
	entering(here, caller)

	val result = register.isEmpty()

	if(isTrace(here)) println ("$here: output result $result")
        return result
     }

    fun isStored (urlTyp: PortType): Boolean {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println ("$here: input urlTyp '$urlTyp'")
	
	val result = if (register.containsKey(urlTyp)) {
	    (register.get(urlTyp)!!.isEmpty())
	}
	else {false}
	
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }
    
    fun store (urlTyp: PortType, urlVal: PortValue) {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println ("$here: input urlTyp '$urlTyp'")

	if (isStored(urlTyp)) {
	    val value = retrieve(urlTyp)
	    if (value != urlVal) {
		fatalErrorPrint("already stored Port Value '$value' for Port Type '$urlTyp' were equal to new one", urlVal.toString(), "Check", here)
	    }
	}
	else {
	    register.put(urlTyp, urlVal)
	}
	if(isTrace(here)) println ("$here: (urlType, urlValue) couple has been stored")
    }
    
    fun retrieve(urlTyp: PortType): PortValue? {
         val (here, caller) = hereAndCaller()
    	 entering(here, caller)

	 val result = register.get(urlTyp)
	 
	 if (result!!.isEmpty()) {
	   fatalErrorPrint("an Port Value existed for Port Type '$urlTyp'", "it did not","Check", here)
         }
	 if(isTrace(here)) println ("$here: output result '$result'")
	 exiting(here)
	 return result
    }
}



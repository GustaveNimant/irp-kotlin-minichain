package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import java.io.File
import java.util.Stack

/**
 * What is it : the Register storing an Url knowing its Type.
 * Example : (LocalIpfsApi, "127.0.0.1:5001") 
 * What to do : provide host and port by asking if stored in ParameterMap 
 * Author : Emile Achadde 25 février 2020 at 19:03:02+01:00
 */

class UrlProvider {

    val register = UrlRegister()
    
    private fun build(host: String, port: Int): UrlValue {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println ("$here: input host '$host'")
	if(isTrace(here)) println ("$here: input port '$port'")
    
	val result = UrlValue(host, port)
	
	if(isTrace(here)) println ("$here: output result $result")
	
	exiting(here)
	return result
    }
    
    fun hostNameFromParameterMap(): String {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	val result = 
	    if (ParameterMap.containsKey("host")) {
		(ParameterMap.getValue("host")).first()
	    }
	else {
	    "127.0.0.1"
	}

	if(isTrace(here)) println ("$here: output result $result")
	return result 
    }
    
    private fun buildAndStoreUrl(UrlTyp: UrlType, host: String, port: Int) {
	val (here, caller) = hereAndCaller()
	entering(here, caller)

	if(isTrace(here)) println ("$here: input UrlTyp '$UrlTyp'")
	if(isTrace(here)) println ("$here: input host '$host'")
	if(isTrace(here)) println ("$here: input port '$port'")
    
	val UrlVal = build(host, port)
	register.store (UrlTyp, UrlVal)
	
	exiting(here)
	return
    }
    
    fun provideUrl(UrlTyp: UrlType) : UrlValue {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	if (register.isEmpty()){
	    val host = hostNameFromParameterMap()
	    val porTyp = portTypeFromParameterMap() 
	    val port = providePort(porTyp)   
	    buildAndStoreUrl(UrlTyp, host, port)
	}
	
	val result = register.retrieve(UrlTyp)!!
	
	if (isTrace(here)) println("$here: output result '$result'")
	exiting(here)
	return result
    }
    
}

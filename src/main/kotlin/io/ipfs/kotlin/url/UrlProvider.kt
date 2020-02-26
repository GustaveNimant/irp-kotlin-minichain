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
    
    private fun build(host: String, porVal: PortValue): UrlValue {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println ("$here: input host '$host'")
	if(isTrace(here)) println ("$here: input porVal '$porVal'")
    
	val result = UrlValue(host, porVal)
	
	if(isTrace(here)) println ("$here: output result $result")
	
	exiting(here)
	return result
    }

    fun portTypeFromParameterMap(): PortType {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	val result = 
	    if (ParameterMap.containsKey("port")) { 
              val wor = ParameterMap.getValue("port").first() // -port <type> [<integer>] 
	      portTypeOfWord(wor)
	    }
	else {
	    PortType.PortWebui
	}
	
	if(isTrace(here)) println ("$here: output result $result")

	exiting(here)
	return result 
    }

    private fun hostNameFromParameterMap(): String {
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
    
    private fun buildAndStoreUrl(UrlTyp: UrlType, host: String, porVal: PortValue) {
	val (here, caller) = hereAndCaller()
	entering(here, caller)

	if(isTrace(here)) println ("$here: input UrlTyp '$UrlTyp'")
	if(isTrace(here)) println ("$here: input host '$host'")
	if(isTrace(here)) println ("$here: input porVal '$porVal'")
    
	val UrlVal = build(host, porVal)
	register.store (UrlTyp, UrlVal)
	
	exiting(here)
	return
    }
    
    public fun provideUrl(UrlTyp: UrlType) : UrlValue {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	if (register.isEmpty()){
	    val host = hostNameFromParameterMap()
	    val porPro = PortProvider ()
	    val porTyp = portTypeFromParameterMap() 
	    val porVal = porPro.providePort(porTyp)   
	    buildAndStoreUrl(UrlTyp, host, porVal)
	}
	
	val result = register.retrieve(UrlTyp)!!
	
	if (isTrace(here)) println("$here: output result '$result'")
	exiting(here)
	return result
    }

    public fun printOfUrlType (UrlTyp: UrlType) {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	val url = provideUrl (UrlTyp)
	val str = url.toString()
	println ("UrlType $UrlTyp => $str")
	exiting(here)
    }

}

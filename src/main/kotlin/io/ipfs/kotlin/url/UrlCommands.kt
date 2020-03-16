package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import io.ipfs.kotlin.defaults.*

import java.util.Stack
import kotlin.system.exitProcess

/**
 * Author : Emile Achadde 27 février 2020 at 14:04:42+01:00
 * Revision : Registers are singletons by Emile Achadde 11 mars 2020 at 17:17:18+01:00
 * Revision : Server SunHttp by Emile Achadde 16 mars 2020 at 10:29:58+01:00
 */

fun executeHostOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -host <HostType> <domain-name>

    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)

    val hosReg = HostRegister
    
    try {
	val wor = wor_s.pop()
	if(isLoop(here)) println("$here: wor '$wor'")
	val hosTyp = HostType.make (wor)
 		try {val worNex = wor_s.pop()
		if(isLoop(here)) println("$here: worNex '$worNex'")
		val hosVal = HostValue(worNex)
		hosReg.store(hosTyp, hosVal)
		} // try
		catch (e: java.util.EmptyStackException) {
		val worNex =
		    when (hosTyp) {
			is HostType.HostLocal -> {"localhost"}
			is HostType.HostUserDefined,
			is HostType.HostRemote -> {
			    fatalErrorPrint("<host-value> were defined","none","enter -host $wor <value>",here)
			}		    
		    } // when hosTyp
		if(isDebug(here)) println("$here: Host Value set to default '$worNex'")
		val hosVal = HostValue(worNex)
		hosReg.store(hosTyp, hosVal)
		} // catch no value
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint("command were <host-type> <host-value>","none","enter -host type value",here)
    } 
    
    if(isTrace(here)){
    	println ("Host Register is:")
	hosReg.print() 
    }
    exiting(here)
}

fun executePortOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -port gateway|jetty|webui|user <Integer>
    
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)

    val porReg = PortRegister
    
    try {
	val wor = wor_s.pop()
	if(isVerbose(here)) println("$here: wor '$wor'")
	    
	val porTyp = PortType.make (wor)
	if(isVerbose(here)) println("$here: porTyp '$porTyp'")
	
	try { val worNex = wor_s.pop()
	      if(isLoop(here)) println("$here: worNex '$worNex'")
	      val int: Int = worNex.toInt() 
	      val porVal = PortValue(int)
	      porReg.store(porTyp, porVal)
	} // try
	catch (e: java.util.EmptyStackException) {
	    val porInt = 
		when (porTyp) {
		    is PortType.PortGateway -> {8080}
		    is PortType.PortJetty -> {9000}
		    is PortType.PortSunHttp -> {8000}
		    is PortType.PortUserDefined -> {5001}		    
		    is PortType.PortWebui -> {5021}
		} // when porTyp
	    if(isDebug(here)) println("$here: Port Value set to default '$porInt'")
	    val porVal = PortValue(porInt)
	    porReg.store(porTyp, porVal)
	} // catch no value 
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint("command were <port-type> <port-value>","none","enter -port type value",here)
	} 
    
    if(isTrace(here)){
    	println ("Port Register is:")
	PortRegister.print() 
    }

    exiting(here)
}

fun executeUrlOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -url localIpfsApi|localServer|remote
    
    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    try {val urlStr = wor_s.pop()
	 println ("$here: urlStr '$urlStr'")
	 val urlTyp = UrlType.make(urlStr)
	 val proUrl = UrlProvider()
	 proUrl.provideOfUrlType(urlTyp)
    }
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("urltype were localIpfsApi|localServer|remote","urltype is empty", "enter url localIpfsApi|localServer|remote", here)
    }

    exiting(here)
}


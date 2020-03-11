package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import io.ipfs.kotlin.defaults.*

import java.util.Stack
import kotlin.system.exitProcess

/**
 * Author : Emile Achadde 27 février 2020 at 14:04:42+01:00
 * Revision : Emile Achadde 11 mars 2020 at 17:17:18+01:00
 */

fun executeHostOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -host <HostType> <Integer>
    var done = false
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)

    val hosReg = HostRegister()
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    if(isLoop(here)) println("$here: wor '$wor'")
	    
	    val hosTyp = HostType.make (wor)
	    when (hosTyp) {
		is HostType.HostUserDefined,
		is HostType.HostLocal,
		is HostType.HostRemote -> {
 		    val worNex = wor_s.pop()
		    if(isLoop(here)) println("$here: worNex '$worNex'")
		    val hosVal = HostValue(worNex)
		    hosReg.store(hosTyp, hosVal)
		}		    
	    } // when hosTyp
	    } // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	    
    } // while

    if(isTrace(here)){
    	println ("Host Register is:")
	hosReg.print() 
    }
    exiting(here)
}

fun executePortOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -port <PortType> <Integer>
    
    var done = false
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)

    val porReg = PortRegister
    
    try {
	val wor = wor_s.pop()
	if(isLoop(here)) println("$here: wor '$wor'")
	    
	val porTyp = PortType.make (wor)

	try { val worNex = wor_s.pop()
	      if(isLoop(here)) println("$here: worNex '$worNex'")
	      val int: Int = worNex.toInt() 
	      val porVal = PortValue(int)
	      porReg.store(porTyp, porVal)
	} // try
	catch (e: java.util.EmptyStackException) {
	    val porInt = 
		when (porTyp) {
		    is PortType.PortUserDefined -> {
			5001
		    }		    
		    is PortType.PortGateway -> {
			5011
		    }
		    is PortType.PortWebui -> {
			5021
		    }
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
	for ( (k, v) in PortRegister.portRegisterMap) {
	    println ("$k => $v")
	}
    }
    exiting(here)
}


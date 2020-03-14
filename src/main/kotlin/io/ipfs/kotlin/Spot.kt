package io.ipfs.kotlin

import java.io.File
import java.util.Stack

fun printSpotOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    val spoTyp = try {wor_s.pop()}
    catch (e:java.util.EmptyStackException) {"data"}

    val str = 
	when(spoTyp) {
	    "data" -> {
		provideSpotData()
	    }
	    "help" -> {
		stringHelpOfString("-print spot")
	    }
	    "triple" -> {
		provideSpotTriple()
	    }
	    else -> {
		fatalErrorPrint("-print spot data|triple","'spoTyp'","reset arguments",here)}
	}
    println ()
    println ("Spot $spoTyp:")
    println (str)
    println ()
    
    exiting(here)
}

fun provideSpotTriple(): Triple<String, String, String> {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val peeId = providePeerId()
    val tic = provideTic()
    val ipSys = provideIpAddressOfWord("system")

    val result = Triple (peeId, tic, ipSys)

    if(isTrace(here)) println ("$here: output result '$result'")
    
    exiting(here)
    return result
}

fun provideSpotData(): String {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val (peeId, tic, ipSys) = provideSpotTriple()
    
    var result = ""
    result += "--- # spot for $peeId\n"
    result += "tic: $tic\n"
    result += "ip: $ipSys"
    
    if(isTrace(here)) {
	println ("$here: output result:")
	println (result)
    }
    exiting(here)
    return result 
}

fun provideTic(): String {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val timLon = getTime()
    val result = timLon.toString()

    exiting(here)
    return result
}

fun writeSpotDataOfFilePath(filPat: String) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    if(isDebug(here)) println ("$here: input filPat '$filPat'")

    val str = provideSpotData()
    outputWriteOfFilePath(filPat, str)
    
    exiting(here)
    }

fun writeSpotDataOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    
    val filPat = try {wor_s.pop()}
    catch (e:java.util.EmptyStackException) {
	"./generator/spot.yml"
    }

    if(isDebug(here)) println ("$here: filPat '$filPat'")

    writeSpotDataOfFilePath(filPat)
    
    exiting(here)
}

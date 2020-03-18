package io.ipfs.kotlin

import java.io.File
import java.util.Stack

// https://iph.heliohost.org/cgi-bin/posted.pl
// https://www.postman.com/
// https://docs.postman-echo.com/?version=latest
// https://postman-echo.com/ip
// https://iph.heliohost.org/cgi-bin/remote_addr.pl

/**
 * Improve : missing local Ip from network card 
 * Ex.: --args="-write spot [file-path:.generator/spot.yml]"
 * Ex.: --args="-print spot data"
 * Ex.: --args="-print spot triple"
 * Author : Emile Achadde 17 mars 2020 at 11:24:18+01:00
 */

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

    val ticInt = tic.toInt()
    val ipRedStr = ipSys.replace(".", "")
    val ipSInt = ipRedStr.toInt()
    
    if(isVerbose(here)) println ("$here: ticInt ticInt")
    if(isVerbose(here)) println ("$here: ipSInt ipSInt")

    val spot = ticInt.xor(ipSInt)
    
    var result = ""
    result += "--- # spot for $peeId\n"
    result += "tic: $tic\n"
    result += "ip: $ipSys\n"
    result += "spot: $spot"
    
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

    val timLon = getTime()/1000
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

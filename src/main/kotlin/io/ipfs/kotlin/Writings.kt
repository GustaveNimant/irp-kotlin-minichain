package io.ipfs.kotlin

import java.io.File
import java.util.Stack

fun executeWriteOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -write spot

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    
    try {
	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isVerbose(here)) println("$here: wor '$wor'")
	
	when (wor_3) {
	    "hel" -> {
		wor_s.clear()
		printHelpOfString("-write ")
    	    }
	    "spo" -> {
		writeSpotDataOfWordStack(wor_s)
	    }
	    else -> {
		fatalErrorPrint ("command were '-write spot [<file-path>]'","'$wor'", "Check input", here)
	    } // else
	    } // when (wor_3)
	} // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint("command stack were not empty","it is empty", "Check", here)
    } // catch
	
    exiting(here)
}

fun wrapperExecuteWriteOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)
    executeWriteOfWordStack(wor_s)
    
    exiting(here)
}


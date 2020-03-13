package io.ipfs.kotlin

import io.ipfs.kotlin.url.*
import java.util.Stack

fun executePrintOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -print register port
    // Ex.: -print peerid
    // Ex.: -print ipaddress system
    // Ex.: -port user 5021 -print url localServer
    // Ex.: -hashfunction sha 256 -hashinput some string -print hashtype -print hashinput -print hashvalue"

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    
    try {
	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "reg" -> {printRegisterOfWordStack(wor_s)}
	    "hel" -> {
		wor_s.clear()
		printHelpOfString("-provide ")
    	    }
	    "has" -> {
		printHashOfWord(wor)
	    }
	    "ipa" -> {
		val wor = wor_s.pop()
		printIpAddressOfWord(wor)
	    }
	    "pee" -> {
		val str = providePeerId()
		println ()
		println ("peerId: $str")
		println ()
	    }
	    "tic" -> {
		val str = provideTic()
		println ()
		println ("Tic: $str")
		println ()
	    }
	    "url" -> {
		try {val urlStr = wor_s.pop()
		     println ("$here: urlStr '$urlStr'")
		     val urlTyp = UrlType.make(urlStr)
		     val proUrl = UrlProvider()
		     proUrl.provideOfUrlType(urlTyp)
		}
		catch (e: java.util.EmptyStackException) {
		    fatalErrorPrint ("urltype were localIpfsApi|localServer|remote","urltype is empty", "enter url localIpfsApi|localServer|remote", here)
    }
	    }
	    else -> {
		fatalErrorPrint ("command were '-print hash|help|ipaddress|peerid|register|tic|url'","'$wor'", "Check input", here)
	    } // else
	    } // when (wor_3)
	} // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint("command stack were not empty","it is empty", "Check", here)
    } // catch
	
    exiting(here)
}

fun wrapperExecutePrintOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)
    executePrintOfWordStack(wor_s)
    
    exiting(here)
}


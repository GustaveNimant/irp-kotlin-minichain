package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*
import java.util.Stack
import kotlin.system.exitProcess

/**
 * Execution : gradlew run --args="-ipfs get QmTzX91dhqHRunjCtrt4LdTErREUA5Gg1wFMiJz1bEiQxp"
 * val multihash = LocalIpfs().add.string("test-string").Hash
 * val content = LocalIpfs().get.cat(multihash)
 * val commit = LocalIpfs().info.version()!!.Commit
 * Author : François Colonna 22 février 2020 at 15:32:44+01:00
 */

fun ipfsExecuteOfWordList(wor_l: List<String>) {
    val (here, caller) = hereAndCaller()
    entering(here, caller)
    
    // Ex.: -ipfs add truc much
    var done = false
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = wor.substring(0,3)
	    if(isLoop(here)) println("$here: wor '$wor'")
	    
	    when (wor_3) {
		"add" -> {
		    val mulH = multiHashOfAddWordStack(wor_s)
		    println ("MultiHashType: $mulH")
		    wor_s.clear()
		}
		"com" -> {
		        wor_s.clear()
                        ipfsCommit()
    		}
		"get" -> {
		        wor_s.clear()
			val immCon = ipfsImmutableContentOfGetWordList(wor_l)
			println ("Content:")
			println (immCon.toString())
    		}
		"hel" -> {
		        wor_s.clear()
			val hel_l = helpList()
			val h_l = hel_l.filter({h -> h.contains("-ipfs ")})
			printOfStringList(h_l)
    		}
		"pee" -> {
		        wor_s.clear()
			val peeH = ipfsConfigIdentityPeeId()
			println ("Peerid '$peeH'")
    		}
		else -> {
		    fatalErrorPrint ("command were 'add', 'get'","'"+wor+"'", "Check input", here)
		} // else
	    } // when
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	
    } // while
    exiting(here)
}

fun multiHashOfAddWordStack (wor_s: Stack<String>): MultiHashType {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val word = stringOfGlueOfWordStack(" ", wor_s)
    wor_s.clear()
    if(isTrace(here)) println ("$here: input word '$word'")

    val filCon = // file path case
	if (isFilePathOfWord(word)) {
	    stringReadOfFilePath(word)
	}
    else {
	word
    }
	
    val strH = LocalIpfs().add.string(filCon).Hash
    val result = multiHashTypeOfString(strH)
    if(isTrace(here)) println ("$here: output result '$result'")

    exiting(here)
    return result
}
    
fun ipfsImmutableContentOfGetWordList (wor_l: List<String>): IpfsImmutableContent {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    println("$here: wor_l '$wor_l'")
    val worH =
	if (wor_l.size == 2) {
	    wor_l[1]
	}
    else {
	val str = stringOfGlueOfStringList("\n", wor_l)
	fatalErrorPrint ("one element in get input", str, "Check input", here)
    }
    
    val mulTyp = multiHashTypeOfString(worH)
    println("$here: mulTyp '$mulTyp'")
    val proImm = IpfsImmutableProvider()
    val result = proImm.provide(mulTyp)

    if(isTrace(here)) println ("$here: output result '$result'")
    
    exiting(here)
    return result
}

fun ipfsCommit (): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val result = LocalIpfs().info.version()!!.Commit
    if(isTrace(here)) println ("$here: output result '$result'")
	
    exiting(here)
    return result
}

fun ipfsConfigIdentityPeeId (): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val result = LocalIpfs().info.version()!!.Commit
    if(isTrace(here)) println ("$here: output result '$result'")

    if (result.isNullOrEmpty()){
	fatalErrorPrint("peerid is not empty", "it is empty", "Check Peer", here)
    }
    
    exiting(here)
    return result
}




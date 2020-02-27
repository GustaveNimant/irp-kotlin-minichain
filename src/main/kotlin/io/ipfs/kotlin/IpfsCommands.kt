package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*
import io.ipfs.kotlin.url.*
import java.util.Stack
import kotlin.system.exitProcess

/**
 * Execution : gradlew run --args="-ipfs get QmTzX91dhqHRunjCtrt4LdTErREUA5Gg1wFMiJz1bEiQxp"
 * val multihash = LocalIpfs().add.string("test-string").Hash
 * val content = LocalIpfs().get.cat(multihash)
 * val commit = LocalIpfs().info.version()!!.Commit
 * Author : François Colonna 22 février 2020 at 15:32:44+01:00
 */

fun hostExecuteOfWordList(wor_l: List<String>) {
    val (here, caller) = hereAndCaller()
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
	    
	    val hosTyp = hostTypeOfWord (wor)
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
	for ( (k, v) in hosReg.register) {
	    println ("$k => $v")
	}
    }
    exiting(here)
}

fun ipfsCommit (): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val result = LocalIpfs().info.version()!!.Commit
    if(isTrace(here)) println ("$here: output result '$result'")
	
    exiting(here)
    return result
}

fun ipfsConfigOfWordStack(wor_s: Stack<String>): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    // ( ipfs [ --offline] config ) Identity.PeerID

    val word = stringOfGlueOfWordStack(" ", wor_s)
    wor_s.clear()
    if(isTrace(here)) println ("$here: input word '$word'")

    val result =
	when(word) {
	    "Identity.PeerID" -> {wrapperIdentityPeerID()}
	    else -> {
		fatalErrorPrint("Identity.PeerID", "'"+word+"'", "Check", here)
	    }
	} // when
    
    if(isTrace(here)) println ("$here: output result '$result'")

    exiting(here)
    return result
}

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
		"add" -> { // "-ipfs add ./parser.bnf" "-ipfs add truc much" 
		    val mulH = multiHashOfWordStack(wor_s)
		    println ("MultiHashType: $mulH")
		    wor_s.clear()
		}
		"com" -> { // commit
		        wor_s.clear()
                        ipfsCommit()
    		}
		"con" -> { // config Identity.PeerID
                        val conStr = ipfsConfigOfWordStack(wor_s)
			println ("Config: $conStr")
		        wor_s.clear()
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
		    val peeId = wrapperPeerId() // peerid = "config Identity.PeerID"
		    println("$here: peerid $peeId")
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

fun multiHashOfWordStack (wor_s: Stack<String>): MultiHashType {
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

fun portExecuteOfWordList(wor_l: List<String>) {
    val (here, caller) = hereAndCaller()
    entering(here, caller)
    
    // Ex.: -port <PortType> <Integer>
    var done = false
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)

    val porReg = PortRegister()
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    if(isLoop(here)) println("$here: wor '$wor'")
	    
	    val porTyp = portTypeOfWord (wor)
	    when (porTyp) {
		is PortType.PortUserDefined -> {
 		    val worNex = wor_s.pop()
		    if(isLoop(here)) println("$here: worNex '$worNex'")
		    val int: Int = worNex.toInt() 
		    val porVal = PortValue(int)
		    porReg.store(porTyp, porVal)
		}		    
		is PortType.PortGateway -> {
		    val porVal = PortValue(5001)
		    porReg.store(porTyp, porVal)
		}
		is PortType.PortWebui -> {
		    val porVal = PortValue(5001)
		    porReg.store(porTyp, porVal)
		}
	    } // when porTyp
	    } // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	    
    } // while

    if(isTrace(here)){
    	println ("Port Register is:")
	for ( (k, v) in porReg.register) {
	    println ("$k => $v")
	}
    }
    exiting(here)
}

fun wrapperIdentityPeerID(): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val result =
	try {
	    val peeId = LocalIpfs().peerid.peerId()
	    peeId!!.Key
	}
        catch (e: java.net.UnknownHostException) {
	    fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Host :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)
	}
	
    if(isTrace(here)) println ("$here: output result '$result'")
	
    exiting(here)
    return result
}

fun wrapperPeerId(): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val result =
	try {
	    val peeId = LocalIpfs().peerid.peerId()
	    peeId!!.Key
	}
        catch (e: java.net.UnknownHostException) {
	    fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Host :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)
	}
	
    if(isTrace(here)) println ("$here: output result '$result'")
	
    exiting(here)
    return result
}



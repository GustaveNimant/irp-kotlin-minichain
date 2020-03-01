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

fun executeIpfsOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: (-ipfs) add truc much
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
		"cat" -> { // (-ipfs cat) QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa
                           val immCon = ipfsImmutableValueOfCatWordList(wor_l)
			   println ("Content:")
			   println (immCon.toString())
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
		    when (wor_l.size) {
			2 -> {
			    var hel_l = helpList()
			    val get_l = hel_l.filter({h -> h.contains("get ")})
			    printOfStringList(get_l)
			    wor_s.clear()
			}
			3 -> {val immCon = ipfsImmutableValueOfGetWordList(wor_l)
			      println ("Content:")
			      println (immCon.toString())
			      wor_s.clear()
			}
			else -> {
			    val str = stringOfGlueOfStringList("\n", wor_l)
			    fatalErrorPrint ("one argument (help) or two arguments (type and what) for -ipfs get command", str, "Check input", here)
			}
    		    } // when
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
		    fatalErrorPrint ("command were 'add' 'cat' 'com'mmit 'con'fig 'get' 'hel'p 'pee'rid","'"+wor+"'", "Check input", here)
		} // else
	    } // when
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	
    } // while
    exiting(here)
}

fun ipfsCommit (): String {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val result = LocalIpfs().info.version()!!.Commit
    if(isTrace(here)) println ("$here: output result '$result'")
	
    exiting(here)
    return result
}

fun ipfsConfigOfWordStack(wor_s: Stack<String>): String {
    val (here, caller) = moduleHereAndCaller()
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

fun ipfsImmutableValueOfCatWordList (wor_l: List<String>): IpfsImmutableValue {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Improve double the get case
    
    // (-ipfs cat) QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa
    
    if(isTrace(here)) println("$here: input wor_l '$wor_l'")
    val hash = wor_l[1]
    if(isDebug(here)) println("$here: (hash) = '($hash)'")

    val type = "text" // arbitrary
    val mulTyp = MultiHashType.make (hash)
    val immTyp = IpfsImmutableType.make (type, mulTyp)
    val proImm = IpfsImmutableProvider()
    val result = proImm.provide(immTyp)
    if(isDebug(here)) println("$here: mulTyp '$mulTyp'")
    if(isDebug(here)) println("$here: immTyp '$immTyp'")
    if(isTrace(here)) println ("$here: output result '$result'")
    
    exiting(here)
    return result
}

fun ipfsImmutableValueOfGetWordList (wor_l: List<String>): IpfsImmutableValue {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // (-ipfs get) block QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa
    
    if(isTrace(here)) println("$here: input wor_l '$wor_l'")
    val (type, what) = Pair(wor_l[1], wor_l[2])
    if(isDebug(here)) println("$here: (type, what) = '($type, $what)'")

    val mulTyp = MultiHashType.make (what)
    val immTyp = IpfsImmutableType.make (type, mulTyp)
    val proImm = IpfsImmutableProvider()
    val result = proImm.provide(immTyp)
    if(isDebug(here)) println("$here: mulTyp '$mulTyp'")
    if(isDebug(here)) println("$here: immTyp '$immTyp'")

    if(isTrace(here)) println ("$here: output result '$result'")
    
    exiting(here)
    return result
}

fun wrapperIdentityPeerID(): String {
    val (here, caller) = moduleHereAndCaller()
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
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val result =
	try {
	    val peeId = LocalIpfs().peerid.peerId()
	    if(isDebug(here)) println ("$here: peeId '$peeId'")
	    peeId!!.Key
	}
        catch (e: java.net.UnknownHostException) {
	    fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Host :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)
	}
	
    if(isTrace(here)) println ("$here: output result '$result'")
	
    exiting(here)
    return result
}


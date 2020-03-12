package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*
import io.ipfs.kotlin.url.*
import kotlin.system.exitProcess

import java.util.Stack
import java.io.File

/**
 * Execution : gradlew run --args="-ipfs get QmTzX91dhqHRunjCtr4LdTErREUA5Gg1wFMiJz1bEiQxp"
 * val multihash = LocalIpfs().add.string("test-string").Hash
 * val content = LocalIpfs().get.cat(multihash)
 * val commit = LocalIpfs().info.version()!!.Commit
 * Author : Emile Achadde 22 février 2020 at 15:32:44+01:00
 * Revision : Emile Achadde 02 mars 2020 at 10:24:15+01:00
 * Revision : ImmutableValue by Emile Achadde 12 mars 2020 at 10:58:56+01:00
 */

fun executeIpfsGetOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -ipfs get help
    // Ex.: -ipfs get block QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa	
	   
    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    
    try {
	val wor = wor_s.pop()
	val wor_3 = wor.substring(0,3)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "hel" -> {
		var hel_l = helpList()	
		val get_l = hel_l.filter({h -> h.contains("get ")})
		println()
		printOfStringList(get_l)
		println()
            }		       
	    "blo" -> {
		val immCon = immutableValueOfGetWordStack(wor_s)
		println ("Content:")
		println (immCon.toString())
	    }
	    else -> {
		fatalErrorPrint ("-ipfs get help or -ipfs get block <hash>","'$wor'", "Check input", here)
	    }
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("-ipfs get help or block","empty Stack", "Check input", here)
    }

    if (!wor_s.isEmpty()){
	val str = stringOfGlueOfWordStack(",", wor_s)
	fatalErrorPrint ("-ipfs get command stack were empty","'$str'", "Check input", here)
    }

    exiting(here)
}

fun executeIpfsOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -ipfs add string truc much
    // Ex.: -ipfs config Identity.PeerID
    // Ex.: -ipfs peerid
    // Ex.: -ipfs cat QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa
    // Ex.: -ipfs get block QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa
    
    var done = false
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = wor.substring(0,3)
	    if(isLoop(here)) println("$here: wor '$wor'")
	    
	    when (wor_3) {
		"add" -> { // "-ipfs add dir(ectory)|fil(e)|str(ing) ./parser.bnf" "-ipfs add truc much"
		    val mulH = ipfsAddOfWordStack(wor_s)
		}
		"cat" -> {
		    // -ipfs cat QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa
                    val immCon = immutableValueOfCatWordStack(wor_s)
		    println ()
		    println ("----- Hash Content -----")
		    println (immCon.content)
		    println ("----- End of Hash Content -----")
		    println ()
    		}
		"com" -> {
		    // commit
		    wor_s.clear()
                    ipfsCommit()
    		}
		"con" -> {
		    // -ipfs config Identity.PeerID
                    val conStr = ipfsConfigOfWordStack(wor_s)
		    println()
		    println ("PeerId: $conStr")
		    println()
		    wor_s.clear()
    		}
		"get" -> {
		    executeIpfsGetOfWordStack(wor_s)
		}
		"hel" -> {
		    wor_s.clear()
		    val hel_l = helpList()
		    val h_l = hel_l.filter({h -> h.contains("-ipfs ")})
		    println()
		    printOfStringList(h_l)
		    println()
    		}
		"pee" -> {
		    // -ipfs peerid = "config Identity.PeerID"
		    val peeId = peerIdProvide()
		    println()
		    println ("PeerId: $peeId")
		    println()
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

fun immutableValueOfCatWordStack (wor_s: Stack<String>): ImmutableValue {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Improve double the get case
    // -ipfs cat QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa
    
    if(isTrace(here)) println("$here: input wor_s '$wor_s'")
    val hash = wor_s.pop()
    if(isDebug(here)) println("$here: (hash) = '($hash)'")

    val type = "text" // arbitrary
    val mulTyp = MultiHashType.make (hash)
    val immTyp = ImmutableType.make (type, mulTyp)
    val proImm = ImmutableProvider()
    val result = proImm.provide(immTyp)
    if(isDebug(here)) println("$here: mulTyp '$mulTyp'")
    if(isDebug(here)) println("$here: immTyp '$immTyp'")
    if(isTrace(here)) println ("$here: output result '$result'")
    
    exiting(here)
    return result
}

fun immutableValueOfGetWordStack (wor_s: Stack<String>): ImmutableValue {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // -ipfs get block QmdKAX85S5uVKWx4ds5NdznJPjgsqAATnnkA8nE2bXQSSa
    
    if(isTrace(here)) println("$here: input wor_s '$wor_s'")
    val type = wor_s.pop()
    val what = wor_s.pop()
    if(isDebug(here)) println("$here: (type, what) = '($type, $what)'")

    val mulTyp = MultiHashType.make (what)
    val immTyp = ImmutableType.make (type, mulTyp)
    val proImm = ImmutableProvider()
    val result = proImm.provide(immTyp)
    if(isDebug(here)) println("$here: mulTyp '$mulTyp'")
    if(isDebug(here)) println("$here: immTyp '$immTyp'")

    if(isTrace(here)) println ("$here: output result '$result'")
    
    exiting(here)
    return result
}

fun ipfsAddOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // "-ipfs add dir(ectory)|fil(e)|str(ing) 
    // Ex.: "-ipfs add string truc much" 
 
    var done = false

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = wor.substring(0,3)
	    if(isLoop(here)) println("$here: wor '$wor'")
	    
	    when (wor_3) {
		"dir" -> { // (-ipfs add) dir(ectory) /directory-path
	              val dirPat = wor_s.pop()    
		      val namHasMap = namedHashMapOfDirectoryPath(dirPat)
		      println ("$here: namedHashMap for directory '$dirPat'")

		      for ( (k, v) in namHasMap) {
			  println ("$k => $v")
		      }
		}
		"fil" -> { // (-ipfs add) fil(e) /file-path
	              val filPat = wor_s.pop()    
		      val mulHas = multiHashTypeOfFilePath(filPat)
		      val strHas = mulHas.hashOf()
		      println ("$here: file '$filPat' MultiHashType '$strHas'")
		      val namHasMap = namedHashMapOfFilePath(filPat)
		      println ("$here: namedHashMap for file '$filPat'")
		      for ( (k, v) in namHasMap) {
			  println ("$k => $v")
		      }
    		}
		"str" -> { // (-ipfs add) str(ing) <file_path>|<string>
			   val str = stringOfGlueOfWordStack(" ", wor_s)
			   wor_s.clear()

			   val mulHas = multiHashTypeOfString (str)
			   val strHas = mulHas.hashOf()
			   val namHasMap = namedHashMapOfString(str)
			   if(isDebug(here)) {
			       println ("$here: string '$str' MultiHashType '$strHas'")
			       println ("$here: namedHashMap for string '$str'")
			       for ( (k, v) in namHasMap) {
				   println ("$k => $v")
			       }
			   }
    		}
		"hel" -> {
			val hel_l = helpList()
			val h_l = hel_l.filter({h -> h.contains("-ipfs ")})
			printOfStringList(h_l)
    		}
		else -> {
		    fatalErrorPrint ("token were 'dir'ectory 'fil'e 'hel'p 'str'ing","'"+wor+"'", "Check input", here)
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

    if(wor_s.isEmpty()){
	fatalErrorPrint("-ipfs config Identity.PeerID", "empty word", "add Identity.PeerID", here)
    }

    val word = stringOfGlueOfWordStack(" ", wor_s)
    wor_s.clear()
    if(isTrace(here)) println ("$here: input word '$word'")

    var result = ""
    when(word) {
	"Identity.PeerID" -> {result = peerIdProvide()}
	"help" -> {
	    val hel_l = helpList()
	    val h_l = hel_l.filter({h -> h.contains("config ")})
	    println()
	    printOfStringList(h_l)
	    println()
	}
	else -> {
	    fatalErrorPrint("-ipfs config Identity.PeerID", "'$word'", "Correct command", here)
	}
    } // when
    
    if(isTrace(here)) println ("$here: output result '$result'")

    exiting(here)
    return result
}

fun peerIdProvide(): String {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val result =
	try {
	    val peeId = LocalIpfs().peerid.peerId()
	    if(isDebug(here)) println ("$here: peeId '$peeId'")
	    peeId!!.Value
	}
        catch (e: java.net.UnknownHostException) {
	    fatalErrorPrint ("Connection to 127.0.0.1:5001", "Connection refused", "launch Host :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)
	}
	
    if(isTrace(here)) println ("$here: output result '$result'")
	
    exiting(here)
    return result
}

fun wrapperExecuteIpfsOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")
    try {
	executeIpfsOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5001", "Connection refused", "launch Ipfs :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}


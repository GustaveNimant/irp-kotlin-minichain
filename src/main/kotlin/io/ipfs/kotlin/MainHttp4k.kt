package io.ipfs.kotlin

import kotlin.system.exitProcess
import io.ipfs.kotlin.defaults.*
import io.ipfs.kotlin.url.*

import java.io.File
import java.util.Stack
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * What is it : the Main Menu to manage Http4k commands
 * Revision : Emile Achadde 08 mars 2020 at 12:19:07+01:00
 */

fun commandAndParametersOfStringList(str_l: List<String>): Pair<String, List<String>> {
  val (here, caller) = moduleHereAndCaller()
  entering(here, caller)

  if(isTrace(here)) println("$here: input str_l $str_l")

  val str = str_l.first()
  if(isVerbose(here)) println("$here: for str $str")

  val result = 
      if (str.startsWith('-')) {
	  val command = str.substring(1).toLowerCase()
	  if(isVerbose(here)) println("$here: command set as '$command'")
	  val arg_l = str_l.drop(1)
	  Pair (command, arg_l)
      }
      else {
	  fatalErrorPrint("command starts with '-'",str, "Check", here) 
      }

  if(isTrace(here)) println("$here: output result $result")

  exiting(here)
  return result
}

fun commandSetOfParameterMap (parMap: Map<String, List<String>>): Set<String> {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input parMap $parMap")

    val result = parMap.keys

    if(isTrace(here)) println ("$here: output result $result")

    exiting(here)
    return result 
}

fun date (): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    val result = current.format(formatter)
    return result
}

fun endProgram () {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    println ()
    println("normal termination")
    println(date())
    println ()
    
    exiting(here)
}

fun executePrintOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -print register port
    
    var done = false
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = wor.substring(0,3)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
		"reg" -> {
	              val subWor = wor_s.pop()
	              val subWor_3 = subWor.substring(0,3)
	   	      if(isLoop(here)) println("$here: while subWor '$subWor'")
		      
		      when (subWor) {
 		      	"host" -> {
			    println ("\nHost Register:")
			    HostRegister.print()
			}
 		      	"port" -> {
			    println ("\nPort Register:")
			    PortRegister.print()
			}
 		      	"url" -> {
			    println ("\nUrl Register:")
			    UrlRegister.print()
			}
			else -> {
			    fatalErrorPrint ("$here: command were '-print register port'","'-print register $wor'", "Check input", here)
			}
		    }
		}// when (wor)
		else -> {
		    fatalErrorPrint ("command were '-print register'","'$wor'", "Check input", here)
		} // else
	    } // when (wor_3)
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	
    } // while
    println ()
    exiting(here)
}

fun executeProvideOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -provide peerid
    
    var done = false
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = wor.substring(0,3)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
		"hel" -> {
		    wor_s.clear()
		    printHelpOfString("-provide ")
    		}
		"has" -> {
		    when (wor) {
			"hashtype" -> {
			    val hasFunTyp = hashFunctionType()
			    println ("$here: hashFunctionType $hasFunTyp")
			}
			"hashinput" -> {
			    val hasInpStr = hashInputString()
			    println ("$here: hashInputString $hasInpStr")
			}
			"hashvalue" -> {
			    val hasFunTyp = hashFunctionType()
			    val hasInpStr = hashInputString()
			    val hasValue = hashStringOfTypeOfInput(hasFunTyp, hasInpStr)
			    println ("$here: hashFunctionType '$hasFunTyp'")
			    println ("$here: hashInputString '$hasInpStr'")
			    println ("$here: hash Value '$hasValue'")
			}
			else -> {
			    fatalErrorPrint ("$here: command were '-provide hashtype' or '-provide hashinput' '-provide hashvalue","'-provide $wor'", "Check input", here)
			}
		    }
		}// when (wor)
		"pee" -> {
		    notYetImplemented("peerid")
		}
		else -> {
		    fatalErrorPrint ("command were 'add', 'get'","'"+wor+"'", "Check input", here)
		} // else
	    } // when (wor_3)
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	
    } // while
    exiting(here)
}

fun main(args: Array<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val parMap = parameterMapOfArguments(args)
    ParameterMap = parMap.toMap() // Globalization for Trace ...
    
    if (parMap.size == 0) {
	println ("$here: Commands are:")
	val hel_l = helpList()
	for (hel in hel_l) {
	    println (hel)
	}
	exitProcess(0)
    }

    if(isVerbose(here)) {
	if (parMap.size > 0) {
	    println ("$here: Commands with their parameter list:")
	    for ( (k, v) in parMap) {
		println ("$here: $k => $v")
	    }
	}
    }

    mainMenu(parMap)
    
    endProgram()
    
    exiting(here)
}

fun mainMenu (parMap: Map<String, List<String>>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input parMap $parMap")
    val com_s = commandSetOfParameterMap (parMap)
    if(isTrace(here)) println ("$here: com_s $com_s")

    var step = 0
    for (com in com_s) { 
	step = step + 1
	println("$here: ----- command # $step '$com' -----")
	val com_3 = com.substring(0,3)
	
	val wor_ml = parMap.get(com)
	val wor_l = wor_ml!!.map({w -> w.toString()}) 
	if (isLoop(here)) println("$here: wor_l '$wor_l'")
	
	when (com_3) {
	    "deb", "ent", "loo", "tra", "ver", "whe" -> {
		val str = stringOfStringList(wor_ml)
		println("$here: '$com' activated for '$str' functions")
	    }
	    "end", "exi" -> {endProgram()}
	    "gen" -> {wrapperExecuteGenerateOfWordList(wor_l)}
	    "has" -> {wrapperExecuteHashOfWord(com)}
	    "hel" -> {helpOfParameterMap(parMap)}
	    "hos" -> {wrapperExecuteHostOfWordList(wor_l)}
	    "htt" -> {wrapperExecuteHttp4kOfWordList(wor_l)}
	    "inp" -> {wrapperExecuteInputOfWordList(wor_l)}
	    "ipf" -> {wrapperExecuteIpfsOfWordList(wor_l)}
	    "kwe" -> {wrapperExecuteKeywordOfWordList(wor_l)}
	    "por" -> {wrapperExecutePortOfWordList(wor_l)}
	    "pri" -> {wrapperExecutePrintOfWordList(wor_l)}
	    "pro" -> {wrapperExecuteProvideOfWordList(wor_l)}
	    "ser" -> {wrapperExecuteServerOfWordList(wor_l)}
	    else -> {
		fatalErrorPrint ("command were one of end, exi(t), gen(erate), has(h), hel(p), hos(t), htt(p4k), inp(ut), ipf(s, kwe(xtract), run", "'$com'", "re Run", here)
	    } // else
	} // when
    } // for
    
    exiting(here)
}

fun parameterMapOfArguments(args: Array<String>): Map<String, List<String>> {
  val (here, caller) = moduleHereAndCaller()
  entering(here, caller)

  if(isTrace(here)) println("$here: input args $args")

  var parMap = mutableMapOf<String, List<String>>()

  val arg_l = args.toList()
  val str_ll = stringListListOfDelimiterOfStringList ("-", arg_l)
  if(isVerbose(here)) println("$here: str_ll $str_ll")
  
  for (str_l in str_ll) {
      if(isVerbose(here)) println("$here: for str_l $str_l")
      var (command, par_l) = commandAndParametersOfStringList(str_l)
      if(parMap.contains(command)) {
	  val str_ = command.substring(3)
	  if(isVerbose(here)) println("$here: Warning: command '$command' is repeated")
	  if(isVerbose(here)) println("$here: Warning: to avoid this, modify the end command name '$command' from its 4th character (i.e. modify '$str_')")
	  command = command + "_"
	  if(isVerbose(here)) println("$here: Warning: command has been currently modified to '$command'") 
      }
      parMap.put (command, par_l)
      if(isVerbose(here)) println("$here: command '$command' added with par_l $par_l") 
  } // for arg_l

  val result = parMap.toMap()
  if(isTrace(here)) println("$here: output result $result")

  exiting(here)
  return result
}

fun wrapperExecuteGenerateOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")

    executeGenerateOfWordList(wor_l)
    
    exiting(here)
}

fun wrapperExecuteHashOfWord(wor: String) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val result = 
	when (wor) {
	    "hashfunction" -> hashFunctionType()
	    "hashinput" -> hashInputString()
	    else -> {
	    }
	}
    println ("$here: result $result")
    exiting(here)
}

fun wrapperExecuteHostOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")
    try {
	executeHostOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5001", "Connection refused", "launch Host :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperExecuteInputOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val inpFilPat = try {(ParameterMap.getValue("input")).first()}
                    catch(e:java.util.NoSuchElementException){
			fatalErrorPrint ("command one argument after command -input", "none", "enter -input <file-path>", here)}
		    println("$here: input file path '$inpFilPat'")
    exiting(here)
}

fun wrapperExecuteKeywordOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")

    val inpFilPat = try { (ParameterMap.getValue("input")).first()}
                    catch(e:java.util.NoSuchElementException){
			fatalErrorPrint("an input file path were given","there are none","add '-input <file-name>' to command line",here)
		    }

     val keyValMap = kwextract(inpFilPat)
     println ("Extracted (key, value) from input")
     for ( (k, v) in keyValMap) {
	 println ("$k => $v")
     }
    
    exiting(here)
}

fun wrapperExecutePortOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")
    try {
	executePortOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5001", "Connection refused", "launch Port :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperExecutePrintOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")
    try {
	executePrintOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5001", "Connection refused", "launch Port :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperExecuteProvideOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")
    try {
	executeProvideOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5001", "Connection refused", "launch Port :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}


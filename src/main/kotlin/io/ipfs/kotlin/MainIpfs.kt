package io.ipfs.kotlin

import kotlin.system.exitProcess
import io.ipfs.kotlin.defaults.*
import io.ipfs.kotlin.url.*

import java.io.File
import java.util.Stack

/**
 * What is it : the Main Menu to manage Ipfs commands
 * Author : Emile Achadde 02 mars 2020 at 10:29:34+01:00
 * Revision : Help.kt extracted by Emile Achadde 02 mars 2020 at 10:30:26+01:00
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

fun endProgram () {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    println("\nnormal termination")
    exiting(here)
}

fun main(args: Array<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val parMap = parameterMapOfArguments(args)
    ParameterMap = parMap.toMap() // Globalization for Trace ...
    
    if (parMap.size == 0) {
	println ("Commands are:")
	val hel_l = helpList()
	for (hel in hel_l) {
	    println (hel)
	}
	exitProcess(0)
    }

    if(isVerbose(here)) {
	if (parMap.size > 0) {
	    println ("Commands with their parameter list:")
	    for ( (k, v) in parMap) {
		println ("$k => $v")
	    }
	}
    }
    
    mainMenu(parMap)
    
    println("\nnormal termination")
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
	    "deb", "loo", "tra", "ver", "whe" -> {
		val str = stringOfStringList(wor_ml)
		println("$here: '$com' activated for '$str' functions")
	    }
	    "end", "exi" -> {endProgram()}
	    "hel" -> {helpOfParameterMap(parMap)}
	    "gen" -> {wrapperExecuteGenerateOfWordList(wor_l)}
	    "hos" -> {wrapperExecuteHostOfWordList(wor_l)}
	    "ipf" -> {wrapperExecuteIpfsOfWordList(wor_l)}
	    "por" -> {wrapperExecutePortOfWordList(wor_l)}
	    "pro" -> {wrapperExecuteProvideOfWordList(wor_l)}
	    else -> {
		fatalErrorPrint ("command were one of end, exi[t], hel[p], ipf[s], run", "'"+com+"'", "re Run", here)
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
	    if(isLoop(here)) println("$here: wor '$wor'")
	    
	    when (wor_3) {
		"pee" -> {
		    notYetImplemented("peerid")
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


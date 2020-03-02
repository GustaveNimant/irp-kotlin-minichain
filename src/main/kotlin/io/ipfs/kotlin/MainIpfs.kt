package io.ipfs.kotlin

import kotlin.system.exitProcess
import io.ipfs.kotlin.defaults.*
import io.ipfs.kotlin.url.*

import java.io.File
import java.util.Stack
import java.lang.Character.MIN_VALUE as nullChar

fun commandAndParametersOfStringList(str_l: List<String>): Pair<String, List<String>> {
  val (here, caller) = moduleHereAndCaller()
  entering(here, caller)

  if(false) println("$here: input str_l $str_l")

  val str = str_l.first()
  if(false) println("$here: for str $str")

  val result = 
      if (str.startsWith('-')) {
	  val command = str.substring(1).toLowerCase()
	  if(false) println("$here: command set as '$command'")
	  val arg_l = str_l.drop(1)
	  Pair (command, arg_l)
      }
      else {
	  fatalErrorPrint("command starts with '-'",str, "Check", here) 
      }

  if(false) println("$here: output result $result")

  exiting(here)
  return result
}

fun commandSetOfParameterMap (parMap: Map<String, List<String>>): Set<String> {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(false) println ("$here: input parMap $parMap")
    val result = parMap.keys

    if(false) println ("$here: output result $result")
    exiting(here)
    return result 
    }

fun endProgram () {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    println("\nnormal termination")
    exiting(here)
}

fun helpList(): List<String> {
    var hel_l = listOf(
	"gradlew [-q] build [--info]",
	"gradlew run --args=\"-help ''|all|compile|host|port|run|url",
	"gradlew run --args=\"-debug <function name>|all\"",
	"gradlew run --args=\"-gen pro(vider)|reg(ister)|typ()e|val(ue) <module-name> <abbreviation>",
	"gradlew run --args=\"-gen generates one of the four kind of modules above",
	"example : gradlew run --args=\"-gen pro immutable imm\"",
	"gradlew run --args=\"-ipfs add [Options] <path> : add a file or a directory to Ipfs https://docs.ipfs.io/reference/api/cli/#ipfs-add",
	"gradlew run --args=\"-ipfs add <path> : add a file or a directory to Ipfs",
	"gradlew run --args=\"-ipfs add <string> : add a string to Ipfs",
	"gradlew run --args=\"-ipfs get <type> <MultiHash>",
	"gradlew run --args=\"-ipfs get help",
	"gradlew run --args=\"-ipfs peerid (ipfs --offline config Identity.PeerID)",
	"gradlew run --args=\"-trace <function name>|all\" print input and output data",
	"gradlew run --args=\"-verbose<function name>|all\"",
	"gradlew run --args=\"-loop<function name>|all\" print message inside a loop",
	"gradlew run --args=\"-when<function name>|all\" print message inside a when",
	"gradlew run --args=\"-port 5122\" defines port with host default (127.0.0.1)",
	"gradlew run --args=\"-host 127.0.0.1|<host name>\" defines host with port default (5001)",
	"gradlew run --args=\"-url 127.0.0.1|<host name>:5001<port>\" defines an url"
	)
    return hel_l
}

fun helpListOfStringList(str_l: List<String>): List<String> {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(true) println ("$here: input str_l $str_l")
    var hel_l = helpList()
    
    val result = 
	if (str_l.isNullOrEmpty()) { // just -help
	   hel_l
	}
        else {
	    var mut_l = mutableListOf<String>()
	    
	    for (str in str_l) {
		val helps =
		    when (str) {
			"all" -> hel_l
			"ipfs" -> hel_l.filter({h -> h.contains("-ipfs ")})
			"run" -> hel_l.filter({h -> h.contains(" run ")})
			"compile" -> listOf("gradlew -q build --info")
			"host" -> listOf("gradlew run --args=\"-host 127.0.0.1|<host name>\" defines host with port default (5001)")
			"port" -> listOf("gradlew run --args=\"-port 5122\" defines port with host default (127.0.0.1)")
			"url" -> listOf("gradlew run --args=\"-url 127.0.0.1|<host name>:5001<port>\" defines an url")
			else -> hel_l
	    } // when
		mut_l.addAll(helps)
	    } // for
	    mut_l.toList()
	} // else
	
	if(isTrace(here)) println ("$here: output result '$result'")
	return result 
}

fun helpOfParameterMap(parMap: Map<String, List<String>>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(true) println ("$here: input parMap $parMap")
    
    if (parMap.containsKey("help"))
    { 
      val str_l = parMap.getValue("help")
      if(true) println ("$here: str_l $str_l")
      val hel_l = helpListOfStringList(str_l)
      printOfStringList(hel_l)

      exiting(here)
      exitProcess(0)
    }
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

    if(false) {
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

    if(false) println ("$here: input parMap $parMap")
    val com_s = commandSetOfParameterMap (parMap)
    if(false) println ("$here: com_s $com_s")

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

  if(false) println("$here: input args $args")

  var parMap = mutableMapOf<String, List<String>>()

  val arg_l = args.toList()
  val str_ll = stringListListOfDelimiterOfStringList ("-", arg_l)
  if(false) println("$here: str_ll $str_ll")
  
  for (str_l in str_ll) {
      if(false) println("$here: for str_l $str_l")
      var (command, par_l) = commandAndParametersOfStringList(str_l)
      if(parMap.contains(command)) {
	  val str_ = command.substring(3)
	  if(false) println("$here: Warning: command '$command' is repeated")
	  if(false) println("$here: Warning: to avoid this, modify the end command name '$command' from its 4th character (i.e. modify '$str_')")
	  command = command + "_"
	  if(false) println("$here: Warning: command has been currently modified to '$command'") 
      }
      parMap.put (command, par_l)
      if(false) println("$here: command '$command' added with par_l $par_l") 
  } // for arg_l

  val result = parMap.toMap()
  if(false) println("$here: output result $result")

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

    if (false) println("$here: input wor_l '$wor_l'")
    try {
	executeIpfsOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Ipfs :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperExecutePortOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (false) println("$here: input wor_l '$wor_l'")
    try {
	executePortOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Port :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperExecuteProvideOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (false) println("$here: input wor_l '$wor_l'")
    try {
	executeProvideOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Port :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperExecuteHostOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (false) println("$here: input wor_l '$wor_l'")
    try {
	executeHostOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Host :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
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


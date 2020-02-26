package io.ipfs.kotlin

import kotlin.system.exitProcess
import io.ipfs.kotlin.defaults.*
import java.io.File
import java.util.Stack
import java.lang.Character.MIN_VALUE as nullChar

fun commandAndParametersOfStringList(str_l: List<String>): Pair<String, List<String>> {
  val (here, caller) = hereAndCaller()
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

fun commandSetOfParameterMap (parM: Map<String, List<String>>): Set<String> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    if(false) println ("$here: input parM $parM")
    val result = parM.keys

    if(false) println ("$here: output result $result")
    exiting(here)
    return result 
    }

fun endProgram () {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    println("\nnormal termination")
    exiting(here)
}

fun helpList(): List<String> {
    var hel_l = listOf(
	"gradlew [-q] build [--info]",
	"gradlew run --args=\"-help ''|all|compile|host|port|run|url",
	"gradlew run --args=\"-debug <function name>|all\"",
	"gradlew run --args=\"-ipfs add [Options] <path> : add a file or a directory to Ipfs https://docs.ipfs.io/reference/api/cli/#ipfs-add",
	"gradlew run --args=\"-ipfs add <path> : add a file or a directory to Ipfs",
	"gradlew run --args=\"-ipfs add <string> : add a string to Ipfs",
	"gradlew run --args=\"-ipfs get <MultiHash> ",
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
    val (here, caller) = hereAndCaller()
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

fun helpOfParameterMap(parM: Map<String, List<String>>) {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    if(true) println ("$here: input parM $parM")
    
    if (parM.containsKey("help"))
    { 
      val str_l = parM.getValue("help")
      if(true) println ("$here: str_l $str_l")
      val hel_l = helpListOfStringList(str_l)
      printOfStringList(hel_l)

      exiting(here)
      exitProcess(0)
    }
}

fun main(args: Array<String>) {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val parM = parameterMapOfArguments(args)
    ParameterMap = parM.toMap() // Globalization for Trace ...
    
    if (parM.size == 0) {
	println ("Commands are:")
	val hel_l = helpList()
	for (hel in hel_l) {
	    println (hel)
	}
	exitProcess(0)
    }

    if(false) {
	if (parM.size > 0) {
	    println ("Commands with their parameter list:")
	    for ( (k, v) in parM) {
		println ("$k => $v")
	    }
	}
    }
    
    mainMenu(parM)
    
    println("\nnormal termination")
    exiting(here)
}

fun mainMenu (parM: Map<String, List<String>>) {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    if(false) println ("$here: input parM $parM")
    val com_s = commandSetOfParameterMap (parM)
    if(false) println ("$here: com_s $com_s")

    var step = 0
    for (com in com_s) { 
	step = step + 1
	println("$here: ----- command # $step '$com' -----")
	val com_3 = com.substring(0,3)
	
	val wor_ml = parM.get(com)
	val wor_l = wor_ml!!.map({w -> w.toString()}) 
	if (isLoop(here)) println("$here: wor_l '$wor_l'")
	
	when (com_3) {
	    "deb", "loo", "tra", "ver", "whe" -> {
		val str = stringOfStringList(wor_ml)
		println("$here: '$com' activated for '$str' functions")
	    }
	    "end", "exi" -> {endProgram()}
	    "hel" -> {helpOfParameterMap(parM)}
	    "hos" -> {wrapperHostExecuteOfWordList(wor_l)}
	    "ipf" -> {wrapperIpfsExecuteOfWordList(wor_l)}
	    "por" -> {wrapperPortExecuteOfWordList(wor_l)}
	    else -> {
		fatalErrorPrint ("command were one of end, exi[t], hel[p], ipf[s], run", "'"+com+"'", "re Run", here)
	    }//catch
	}
    } // for
    
    exiting(here)
}

fun parameterMapOfArguments(args: Array<String>): Map<String, List<String>> {
  val (here, caller) = hereAndCaller()
  entering(here, caller)

  if(false) println("$here: input args $args")

  var parM = mutableMapOf<String, List<String>>()

  val arg_l = args.toList()
  val str_ll = stringListListOfDelimiterOfStringList ("-", arg_l)
  if(false) println("$here: str_ll $str_ll")
  
  for (str_l in str_ll) {
      if(false) println("$here: for str_l $str_l")
      var (command, par_l) = commandAndParametersOfStringList(str_l)
      if(parM.contains(command)) {
	  val str_ = command.substring(3)
	  if(false) println("$here: Warning: command '$command' is repeated")
	  if(false) println("$here: Warning: to avoid this, modify the end command name '$command' from its 4th character (i.e. modify '$str_')")
	  command = command + "_"
	  if(false) println("$here: Warning: command has been currently modified to '$command'") 
      }
      parM.put (command, par_l)
      if(false) println("$here: command '$command' added with par_l $par_l") 
  } // for arg_l

  val result = parM.toMap()
  if(false) println("$here: output result $result")

  exiting(here)
  return result
}

fun wrapperIpfsExecuteOfWordList (wor_l: List<String>) {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    if (false) println("$here: input wor_l '$wor_l'")
    try {
	ipfsExecuteOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Ipfs :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperPortExecuteOfWordList (wor_l: List<String>) {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    if (false) println("$here: input wor_l '$wor_l'")
    try {
	portExecuteOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Port :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperHostExecuteOfWordList (wor_l: List<String>) {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    if (false) println("$here: input wor_l '$wor_l'")
    try {
	hostExecuteOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5122", "Connection refused", "launch Host :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}


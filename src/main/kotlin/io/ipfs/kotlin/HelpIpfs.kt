package io.ipfs.kotlin

import kotlin.system.exitProcess
import io.ipfs.kotlin.defaults.*
import java.io.File
import java.util.Stack

fun helpList(): List<String> {
    var hel_l = listOf(
	"gradlew [-q] build [--info]",
	"gradlew run --args=\"-help ''|all|compile|host|port|run|url",
	"gradlew run --args=\"-debug <function name>|all\"",
	"gradlew run --args=\"-exa(mple) (https://github.com/http4k/http4k/blob/master/src/docs/cookbook/readme/example.kt)\"",
	"gradlew run --args=\"-gen pro(vider)|reg(ister)|typ()e|val(ue) <module-name> <abbreviation>",
	"gradlew run --args=\"-gen generates one of the four kind of modules above",
	"gradlew run --args=\"-gen pro immutable imm\" (example)",
	"gradlew run --args=\"-http4k get (client OkHttp)",
	"gradlew run --args=\"-http4k example (launches serveur Jetty on localhost:9000)",
	"gradlew run --args=\"-http4k quickstart (elementary example server + client)",
	"gradlew run --args=\"-host 127.0.0.1|<host name>\" defines host with port default (5001)",
	"gradlew run --args=\"-ipfs add [Options] <path> : add a file or a directory to Ipfs https://docs.ipfs.io/reference/api/cli/#ipfs-add",
	"gradlew run --args=\"-input <file-path> : stores file-path in ParameterMap",
	"gradlew run --args=\"-ipfs add <path> : add a file or a directory to Ipfs",
	"gradlew run --args=\"-ipfs add <string> : add a string to Ipfs",
	"gradlew run --args=\"-ipfs config Identity.PeerID : get PeerId",
	"gradlew run --args=\"-ipfs config help",
	"gradlew run --args=\"-ipfs get block <MultiHash>",
	"gradlew run --args=\"-ipfs get help",
	"gradlew run --args=\"-ipfs peerid (same as ipfs config Identity.PeerID)",
	"gradlew run --args=\"-kwe(xtract) -input <file-path> (extract keyword value couple",
	"gradlew run --args=\"-loop<function name>|all\" print message inside a loop",
	"gradlew run --args=\"-port 5001\" defines port with host default (127.0.0.1)",
	"gradlew run --args=\"-print register port",
	"gradlew run --args=\"-provide peerid",
	"gradlew run --args=\"-provide hashtype|hashinput|hashvalue",
	"gradlew run --args=\"-qui(ckStart) (https://www.http4k.org/quickstart/)",
	"gradlew run --args=\"-trace <function name>|all\" print input and output data",
	"gradlew run --args=\"-verbose<function name>|all\"",
	"gradlew run --args=\"-when<function name>|all\" print message inside a when",
	"gradlew run --args=\"-url 127.0.0.1|<host name>:5001<port>\" defines an url"
	)
    return hel_l
}

fun helpListOfStringList(str_l: List<String>): List<String> {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input str_l $str_l")
    var hel_l = helpList()
    
    val result = 
	if (str_l.isNullOrEmpty()) { // just -help
	   hel_l
	}
        else {
	    var mut_l = mutableListOf<String>()
	    
	    for (str in str_l) {
		if(isLoop(here)) println ("$here: for str '$str'")
		val helps =
		    when (str) {
			"all" -> hel_l
			"ipfs" -> hel_l.filter({h -> h.contains("-ipfs ")})
			"run" -> hel_l.filter({h -> h.contains(" run ")})
			"compile" -> listOf("gradlew -q build --info")
			"host" -> listOf("gradlew run --args=\"-host 127.0.0.1|<host name>\" defines host with port default (5001)")
			"port" -> listOf("gradlew run --args=\"-port 5001\" defines port with host default (127.0.0.1)")
			"url" -> listOf("gradlew run --args=\"-url 127.0.0.1|<host name>:5001<port>\" defines an url")
			else -> listOf()
		    } // when
		if(isLoop(here)) println ("$here: for helps '$helps'")
		if(!helps.isNullOrEmpty()) mut_l.addAll(helps)
	    } // for
	    mut_l.toList()
	} // else

	if(result.isNullOrEmpty()){
	    fatalErrorPrint("at least one item of '$str_l' were defined in helpList() and Menu","none is defined","add them to helpList() and Menu", here)
	}
	if(isTrace(here)) println ("$here: output result '$result'")
	return result 
}

fun helpOfParameterMap(parMap: Map<String, List<String>>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input parMap $parMap")
    
    if (parMap.containsKey("help"))
    { 
      val str_l = parMap.getValue("help")
      if(isVerbose(here)) println ("$here: str_l $str_l")
      val hel_l = helpListOfStringList(str_l)
      printOfStringList(hel_l)

      exiting(here)
      exitProcess(0)
    }
}

fun printHelpOfString(str: String) {
    val hel_l = helpList()
    val h_l = hel_l.filter({h -> h.contains(str)})
    println()
    printOfStringList(h_l)
    println()
}

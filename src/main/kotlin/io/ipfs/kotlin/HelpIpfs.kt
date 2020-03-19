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
	"gradlew run --args=\"-generate pro(vider)|reg(ister)|typ()e|val(ue) <module-name> <abbreviation>",
	"gradlew run --args=\"-gen generates one of the four kind of modules above",
	"gradlew run --args=\"-gen pro immutable imm\" (example)",
	"gradlew run --args=\"-host 127.0.0.1|<host name>\" defines host with port default (5001)",
	"gradlew run --args=\"-http4k client get http://82.67.137.54/js/files/test.json\"",
	"gradlew run --args=\"-http4k client get https://xkcd.com/info.0.json\"",
	"gradlew run --args=\"-http4k client response\"",
	"gradlew run --args=\"-http4k forms lens\"",
	"gradlew run --args=\"-http4k forms standard\"",
	"gradlew run --args=\"-http4k get port 9000 host localhost route /greet/Jules\"",
	"gradlew run --args=\"-http4k get port <port-type> host <host-type> route <route>(client OkHttp)",
	"gradlew run --args=\"-http4k inmemory (server + client /greet/Bob)",
	"gradlew run --args=\"-http4k ipfs get stat etc/spot.yml",
	"gradlew run --args=\"-http4k ipfs post (post spotData)",
	"gradlew run --args=\"-http4k quickstart (elementary example server + client)",
	"gradlew run --args=\"-http4k server filtered\"",
	"gradlew run --args=\"-http4k server function\"",
	"gradlew run --args=\"-http4k server jetty full",
	"gradlew run --args=\"-ipfs add [Options] <path> : add a file or a directory to Ipfs https://docs.ipfs.io/reference/api/cli/#ipfs-add",
	"gradlew run --args=\"-input <file-path> : stores file-path in ParameterMap",
	"gradlew run --args=\"-ipaddress local|system",
	"gradlew run --args=\"-ipfs add <path> : add a file or a directory to Ipfs",
	"gradlew run --args=\"-ipfs add <string> : add a string to Ipfs",
	"gradlew run --args=\"-ipfs config Identity.PeerID : get PeerId",
	"gradlew run --args=\"-ipfs config help",
	"gradlew run --args=\"-ipfs get block <MultiHash>",
	"gradlew run --args=\"-ipfs get help",
	"gradlew run --args=\"-ipfs peerid (same as ipfs config Identity.PeerID)",
	"gradlew run --args=\"-kwe -input <yml-file-path> (extract keyword value couple)",
	"gradlew run --args=\"-kwe -input /home/achadde/sources/irp-kotlin-minichain/YMLS/current-block.yml",
	"gradlew run --args=\"-loop<function name>|all\" print message inside a loop",
	"gradlew run --args=\"-port gateway|jetty|webui|user <integer>",
	"gradlew run --args=\"-port webui 5001 -write spot\"",
	"gradlew run --args=\"-print register host|port|url",
	"gradlew run --args=\"-print register port",
	"gradlew run --args=\"-print spot data|triple",
	"gradlew run --args=\"-provide peerid",
	"gradlew run --args=\"-provide hashtype|hashinput|hashvalue",
	"gradlew run --args=\"-qui(ckStart) (https://www.http4k.org/quickstart/)",
	"gradlew run --args=\"-trace <function name>|all\" print input and output data",
	"gradlew run --args=\"-verbose<function name>|all\"",
	"gradlew run --args=\"-when<function name>|all\" print message inside a when",
	"gradlew run --args=\"-write spot <file-path:./generator/spot.yml>",
	"gradlew run --args=\"-url localIpfsApi|localServer|remote (uses -port and -host)\""
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

fun printHelpOfString(str: String) {
    val strHel = stringHelpOfString(str)
    println()
    println(strHel)
    println()
}

fun printHelpOfStringList(str_l: List<String>) {
    val strHel = stringHelpOfStringList(str_l)
    println()
    println(strHel)
    println()
}

fun stringHelpOfString(str: String): String {
    val hel_l = helpList()
    val h_l = hel_l.filter({h -> h.contains(str)})
    val result = stringOfGlueOfStringList("\n", h_l)
    return result
}

fun stringHelpOfStringList(str_l: List<String>): String {
    var hel_l = helpList()
    var h_l = listOf("")
    for (str in str_l) {
	h_l = hel_l
	hel_l = h_l.filter({h -> h.contains(str)})
    }
    val result = stringOfGlueOfStringList("\n", hel_l)
    return result
}

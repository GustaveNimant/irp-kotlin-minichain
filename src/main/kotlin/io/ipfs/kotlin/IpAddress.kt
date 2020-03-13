package io.ipfs.kotlin

import java.net.*;
import java.io.*; 
import java.util.*; 
import java.net.InetAddress; 

fun localIpAddress(): String {
    val localHost = InetAddress.getLocalHost()
    val result = (localHost.getHostAddress()).trim()
    return result
}

fun printIpAddressOfWord(wor: String) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor '$wor'")

    val ipAdd = provideIpAddressOfWord(wor)
    println()
    println ("$wor Ip $ipAdd")
    println()

    exiting(here)
}

fun provideIpAddressOfWord(wor: String): String {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
    
    val result = 
	when (wor_3) {
	    "loc" -> {localIpAddress()}
	    "sys" -> {systemIpAddress()}
	    else -> {
		fatalErrorPrint ("$here: kind of Ip were local|system","'$wor'", "Check input", here)
	}
    }
    exiting(here)
    return result
}
    
fun systemIpAddress(): String {
    val url_name = URL("http://bot.whatismyipaddress.com")
    val sc = BufferedReader(InputStreamReader(url_name.openStream())) 
    val result = sc.readLine().trim() 
    return result 
} 


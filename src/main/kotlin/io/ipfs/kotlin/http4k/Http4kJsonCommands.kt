package io.ipfs.kotlin.http4k

import kotlin.system.exitProcess
import io.ipfs.kotlin.defaults.*
import io.ipfs.kotlin.url.*
import io.ipfs.kotlin.*

import java.io.File
import java.util.Stack
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

import org.http4k.format.Jackson
import org.http4k.format.Jackson.asJsonArray
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import org.http4k.format.Jackson.asPrettyJsonString
import org.http4k.format.Jackson.json

import com.beust.klaxon.Klaxon
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.*

data class MyInts(val a: String, val b: Int)

fun http4kKlaxonOnJsonString() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: --args="-http4k jackson object"
    // https://stackoverflow.com/questions/33368328/how-to-use-jackson-to-deserialize-to-kotlin-collections

    val jsonStr = """{"Hash":"QmbFMke1KXqnYyBBWxB74N4c5SBnJMVAiMNRcGu6x1AwQH","Size":0,"CumulativeSize":6,"Blocks":0,"Type":"file"}"""

    val result = Klaxon().parse<MyData>(jsonStr)
	
    println("$here: result starts here ----->")
    println("$here result '$result'")
    val hash = result?.Hash
    println("$here: hash '$hash'")
    println("$here: <----- result ends here")
    exiting(here)
}

fun http4kJacksonObjectMapperOnJsonArray() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: --args="-http4k jackson object"
    // https://stackoverflow.com/questions/33368328/how-to-use-jackson-to-deserialize-to-kotlin-collections

    val jsonStr = """[{"a": "value1", "b": 1}, {"a": "value2", "b": 2}]"""

    val mapper = jacksonObjectMapper()
    val res_l: List<MyInts> = mapper.readValue<List<MyInts>>(jsonStr)
	
    println("$here: result starts here ----->")
    println("$here res_l '$res_l'")
    for (res in res_l) {
	println("$here res '"+res+"'")
	val a = res.a
	val b = res.b
	println("$here a '$a' b '$b'")

	}
    println("$here: <----- result ends here")
    exiting(here)
}

fun http4kJacksonSimpleOnJsonArray() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: --args="-http4k jackson simple"
    // https://stackoverflow.com/questions/33368328/how-to-use-jackson-to-deserialize-to-kotlin-collections

     val jsonStr = """[{"a": "value1", "b": 1}, {"a": "value2", "b": 2}]"""

     val mapper = jacksonObjectMapper()  
     val res_l: List<MyInts> = mapper.readValue(jsonStr)

    println("$here: result starts here ----->")
    println("$here res_l '$res_l'")
    for (res in res_l) {
	println("$here res '"+res+"'")
	val a = res.a
	val b = res.b
	println("$here a '$a' b '$b'")

	}
    println("$here: <----- result ends here")
    exiting(here)
}

fun menuHttp4kJsonOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: -http4k jackson array
    
    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "kla" -> {http4kKlaxonOnJsonString()}
	    "sim" -> {http4kJacksonSimpleOnJsonArray()}
	    "obj" -> {http4kJacksonObjectMapperOnJsonArray()} 
	    else -> {
		fatalErrorPrint ("command were json","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k jackson json","no arguments", "Complete input", here)
    }
    exiting(here)
}


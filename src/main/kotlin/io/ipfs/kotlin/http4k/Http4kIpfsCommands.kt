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

import org.http4k.client.ApacheClient
import org.http4k.client.JavaHttpClient
import org.http4k.client.OkHttp

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.MultipartFormBody
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.core.getFirst
import org.http4k.core.then
import org.http4k.core.toParametersMap
import org.http4k.core.with

import org.http4k.filter.CachingFilters
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.filter.ServerFilters

import org.http4k.lens.FormField
import org.http4k.lens.Header
import org.http4k.lens.LensFailure
import org.http4k.lens.MultipartForm
import org.http4k.lens.MultipartFormField
import org.http4k.lens.MultipartFormFile
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.multipartForm
import org.http4k.lens.webForm
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.SunHttp
import org.http4k.server.asServer

import org.http4k.format.Jackson
import org.http4k.format.Jackson.asJsonArray
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import org.http4k.format.Jackson.asPrettyJsonString
import org.http4k.format.Jackson.json

import com.beust.klaxon.Klaxon

data class MyData(val Hash: String, val Size: Int, val CumulativeSize: Int, val Blocks: Int, val Type: String)

fun http4kIpfsGetStatOfFileName(filNam: String) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -http4k ipfs get stat /etc/spot.json
    
    if(isTrace(here)) println ("$here: input filNam '$filNam'")

    val uri = "http://localhost:5001/api/v0/files/stat"
    val method = Method.GET
    val request = Request(method, uri).query("arg", filNam)
    
    val message = request.toMessage()
    println("$here: message '$message'")
    println()

    val client: HttpHandler = JavaHttpClient()
    println("$here: JavaHttpClient '$client'")
    
    val response = client(request) 
    println("$here: response '$response'")
    
    val body = response.body
    println("$here: body '$body'")

    val jsonStr = body.toString()
    println("$here: jsonStr '$jsonStr'")
    val result = try {
	Klaxon().parse<MyData>(jsonStr)
    }
    catch(e: com.beust.klaxon.KlaxonException)  {
	fatalErrorPrint ("IPFS daemon were launched","it was not","run : jsm; . config.sh; ipmsd.sh", here )
    }
    
    println("$here: result '$result'")
    val hash = result?.Hash
    println("$here: hash '$hash'")

    val blocks = result?.Blocks
    println("$here: blocks '$blocks'")

    val payload = body.payload
    println("$here: payload '$payload'")

    val stream = body.stream
    println("$here: stream '$stream'")

    val status = response.status
    println("$here: status '$status'")
    if (! status.successful) {
	val description = status.description
	val code = status.code
	when(code) {
	    503 ->
		fatalErrorPrint("IPFS server were started ",description," run for example : jsm; . config.sh; ipmsd.sh", here)
	    else ->
		fatalErrorPrint("Response were successful ",description,"Check", here)
	}
    }

    exiting(here)
}

fun http4kIpfsPostWrite() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Improve : file created is empty
    // http://127.0.0.1:5001/api/v0/<command>
    // Ex.: -http4k ipfs post
    
    //val spoDat = provideSpotData()
    val spoDat = """{
	"Hash":"QmZYVoscbWWJJZWy7Ue19iGXC5SRh3kune3gKSYHv3kzKn",
	"tic": 1584440601,
	"ip": "82.67.137.54",
	"spot": 1865810819,
    }"""
    
    val request = Request(Method.POST, "http://localhost:5001/api/v0/files/write")
	.form("file", "./generator/spot.json")
 //     .body(spoDat)
      .query("arg", "/etc/spot.json")
      .query("create", "true")
      .query("parents", "true")
      .header("Content-Type", "multipart/form-data;boundary=immutable-file-boundary-123")
    println()
    println("$here: request '$request'")
    println()

    println("$here: ApacheClient starts here")
    val client = ApacheClient()
    println ("$here: client $client")
    println("$here: ApacheClient ends here")

    println("$here: client(request) starts here")
    
    println(client(request))
    println("$here: client(request) ends here")
    
    exiting(here)
}

fun menuHttp4kIpfsGetOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "sta" -> {
		try {
		    val filNam = wor_s.pop()
		    http4kIpfsGetStatOfFileName(filNam)
		}
		catch(e: java.util.EmptyStackException) {
		    fatalErrorPrint ("file name were provided","no argument", "Check input", here)
		}
	    }
	    "hel" -> {printHelpOfString("ipfs get ")}
	    else -> {
		fatalErrorPrint ("command were 'sta't <file-name>","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k ipfs get 'sta't","no arguments", "Complete input", here)
    }
    exiting(here)
}

fun menuHttp4kIpfsOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "get" -> {menuHttp4kIpfsGetOfWordStack(wor_s)}
	    "pos" -> {http4kIpfsPostWrite()}
	    "hel" -> {printHelpOfString("ipfs ")}
	    else -> {
		fatalErrorPrint ("command were 'get' or 'pos't","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k ipfs 'get' or 'pos't","no arguments", "Complete input", here)
    }
    exiting(here)
}


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
import org.http4k.hamkrest.hasStatus
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
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import org.http4k.routing.static
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun http4kClientAsAFunction () {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // --args="-http4k client func"
    
    val request = Request(Method.GET, "https://xkcd.com/info.0.json")
    val client: HttpHandler = JavaHttpClient()

    println("$here: response ------>")
    val response = client(request)
    println(response)
    println("$here: <------ end of response")
    exiting(here)
}

fun http4kClientGetOfUri (uri: String) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -http4k client get http://82.67.137.54/js/json/files/test.json"

    if(isTrace(here)) println ("$here: input uri '$uri'")

    val request = Request(Method.GET, uri)
    val client: HttpHandler = JavaHttpClient()

    println("$here: response ------>")
    val response = client(request)
    println(response)
    println("$here: <------ end of response")

    exiting(here)
}

fun http4kClientResponse () {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: -http4k client response
    // HTTP clients are also HttpHandlers!
    
    val client: HttpHandler = OkHttp()
    val request = Request(GET, "http://localhost:9000/greet/Bob")

    if(isVerbose(here)){
	println("$here: request starts here")
	println(request)
	println("$here: request ends here")
    }

    val response: Response = client(request)

    if(isVerbose(here)){
	println("$here: response starts here")
	println(response)
	println("$here: response ends here")
    }
    
    val status = response.status
    if(isVerbose(here))println("$here: response status $status")
    if (! status.successful) {
	val description = status.description
	val code = status.code
	when(code) {
	    503 ->
		fatalErrorPrint("server were started ",description," run for example : -http4k server jetty start", here)
	    else ->
		fatalErrorPrint("Response were successful ",description,"Check", here)
	}
    }
// Produces:
//    Request to /api/greet/Bob took 1ms
//    HTTP/1.1 200
//    cache-control: private, must-revalidate
//    content-length: 9
//    date: Thu, 08 Jun 2017 13:01:13 GMT
//    expires: 0
//    server: Jetty(9.3.16.v20170120)
//
//    hello Bob
 
    exiting(here)
}

fun menuHttp4kClientOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    // Ex.: -http4k client function
    // Ex.: -http4k client get http://82.67.137.54/js/json/files/test.json
    
    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "fun" -> {http4kClientAsAFunction()}
	    "get" -> {
		val uri = wor_s.pop()
		http4kClientGetOfUri(uri)
	    }
	    "res" -> {http4kClientResponse()}
	    else -> {
		fatalErrorPrint ("command were 'fun'ction or 'get' or'res'ponse","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k client 'fun'ction or 'get' or 'res'ponse","no arguments", "Complete input", here)
    }
    exiting(here)
}

fun menuHttp4kClientUrlGetOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Same as http4kClientResponse
    // Needs to launch server first (with -http4k server jetty full)
    // Ex.: -http4k get port 9000 host localhost route /greet/Jules

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    if (wor_s.isEmpty()){
	fatalErrorPrint ("command were -http4k get 'hos't or 'por't or 'rou'te ","no get arguments", "Check input", here)
    }

    /* Local variables */
    var hosStr =""
    var porStr =""
    var rouStr =""
    
    var done = false
    while (!done) {
	try {
 	    val wor = wor_s.pop()
	    val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
		"hel" -> {
		    printHelpOfString("get ")
		}
		"hos" -> {
		    hosStr = wor_s.pop()
		}
		"por" -> {
		    porStr = wor_s.pop()
		}
		"rou" -> {
		    rouStr = wor_s.pop()
		    println("$here: when porStr '$porStr'")
		    println("$here: when hosStr '$hosStr'")
		    println("$here: when rouStr '$rouStr'")
		}
		else -> {
		    fatalErrorPrint ("command were 'hos't or 'por't or 'rou'te ","'$wor'", "Check input", here)
		} // else
	    } // when (wor_3)
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	
    } // while

    // HTTP clients are also HttpHandlers!
    val client: HttpHandler = OkHttp()

    println("$here: client OkHttp started")
    println("client")

    val uri = "http://" + hosStr + ":" + porStr + rouStr 

    if(isDebug(here)) println ("$here: uri '$uri'")
    val request = Request(GET, uri)
    val response: Response = client(request)

    println("$here: response")
    println(response)

    val pattern = Regex("Client Error: Connection Refused")
    if(pattern.containsMatchIn(response.toString())){
	fatalErrorPrint("server were started","it is not","run for example : -http4k server jetty start", here)
    }

    exiting(here)
}



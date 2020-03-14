package io.ipfs.kotlin

import kotlin.system.exitProcess
import io.ipfs.kotlin.defaults.*
import io.ipfs.kotlin.url.*

import java.io.File
import java.util.Stack
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.client.OkHttp
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.CachingFilters
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

/**
 * Author : Emile Achadde 12 mars 2020 at 15:13:49+01:00
 */

fun executeExampleOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // we can bind HttpHandlers (which are just functions
    //    from  Request -> Response) to paths/methods to create a Route,
    // then combine many Routes together to make another HttpHandler
    
    val app: HttpHandler = routes(
        "/ping" bind GET to { _: Request -> Response(OK).body("pong!") },
        "/greet/{name}" bind GET to { req: Request ->
            val name: String? = req.path("name")
            Response(OK).body("hello ${name ?: "anon!"}")
        }
    )

    // call the handler in-memory without spinning up a server
    val inMemoryResponse: Response = app(Request(GET, "/greet/Bob"))
    println("$here: inMemoryResponse")
    println(inMemoryResponse)

// Produces:
//    HTTP/1.1 200 OK
//
//
//    hello Bob

    // this is a Filter - it performs pre/post processing on a request or response
    val timingFilter = Filter {
        next: HttpHandler ->
        {
            request: Request ->
            val start = System.currentTimeMillis()
            val response = next(request)
            val latency = System.currentTimeMillis() - start
            println("Request to ${request.uri} took ${latency}ms")
            response
        }
    }

    // we can "stack" filters to create reusable units, and then apply them to an HttpHandler
    val compositeFilter = CachingFilters.Response.NoCache().then(timingFilter)
    val filteredApp: HttpHandler = compositeFilter.then(app)

    // only 1 LOC to mount an app and start it in a container
    filteredApp.asServer(Jetty(9000)).start()

    // HTTP clients are also HttpHandlers!
    val client: HttpHandler = OkHttp()

    val networkResponse: Response = client(Request(GET, "http://localhost:9000/greet/Bob"))
    println("$here: networkResponse")
    println(networkResponse)

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

fun executeHttp4kOfWordList(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -http4k get port <port-type> host <host-type> route <route>
    // Ex.: -http4k quickstart
    // Ex.: -http4k server jetty start
    // Ex.: -http4k example		
    // Port and Host are defined in a previous command

    var done = false
    if(isTrace(here)) println ("$here: input wor_l '$wor_l'")
    var wor_s = wordStackOfWordList(wor_l)
    
    while (!done) {
	try {
 	    val wor = wor_s.pop()
	    val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
	    	 "exa" -> {wrapperExecuteExampleOfWordStack(wor_s)}
		 "get" -> {wrapperExecuteHttp4kGetOfWordStack(wor_s)}
		 "hel" -> {printHelpOfString("-http4k ")}
		 "qui" -> {wrapperExecuteQuickStartOfWordStack(wor_s)}
		 "ser" -> {wrapperExecuteServerOfWordStack(wor_s)}
		else -> {
		    fatalErrorPrint ("command were 'exa'mple or 'get' or 'qui'ckstart","'$wor'", "Check input", here)
		} // else
	    } // when (wor_3)
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	
    } // while
    
    exiting(here)
}


fun executeQuickStartOfWordStack(wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // https://www.http4k.org/quickstart/
    val app = { request: Request -> Response(OK).body("Hello, ${request.query("name")}!") }
    println ("$here: app $app")
    
    jettyServer.start()
    println ("$here: jettyServer started on Port 9000")
    
    val request = Request(Method.GET, "http://localhost:9000").query("name", "John Doe")
    println ("$here: request $request")
    
    val client = ApacheClient()
    println ("$here: client $client")
    
    println(client(request))
    
    jettyServer.stop()

    exiting(here)
}

fun executeServerJettyOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -http4k server jetty start
    // Ex.: -http4k server jetty stop	

    var done = false
    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
		"sta" -> {
		    jettyServer.start()
		    println ("$here: jettyServer started on port 9000")
		    val request = Request(Method.GET, "http://localhost:9000").query("name", "John Doe")
		    println ("$here: request $request")
    
		    val client = ApacheClient()
		    println ("$here: client $client")
    
		    println(client(request))
    		}
		"sto" -> {
		    jettyServer.stop()
		    println ("$here: jettyServer has been stopped")
		}
		else -> {
		    fatalErrorPrint ("$here: command were 'jetty start|stop","'-server $wor'", "Check input", here)
		}
	    }// when (wor)
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
    } // while
    exiting(here)
}

fun executeServerOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -server jetty start
    // Ex.: -server jetty stop	

    var done = false
    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
		"jet" -> {executeServerJettyOfWordStack(wor_s)}
		else -> {
		    fatalErrorPrint ("command were 'jet'ty","'$wor'", "Check input", here)
		} // else
	    } // when (wor_3)
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
	
    } // while
    exiting(here)
}

fun responseFromGetRequestOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Needs to launch server first (with -http4k example)
    // Ex.: -http4k get port 9000 host localhost route /greet/Jules

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

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

    val url = "http://" + hosStr + ":" + porStr + rouStr 

    if(isDebug(here)) println ("$here: url '$url'")
    
    val networkResponse: Response = client(Request(GET, "http://localhost:9000/greet/Bob"))

    println("$here: networkResponse")
    println(networkResponse)

    exiting(here)
}

fun wrapperExecuteExampleOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_s '$wor_s'")

    executeExampleOfWordStack(wor_s)
    
    exiting(here)
}

fun wrapperExecuteHttp4kGetOfWordStack (wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_s '$wor_s'")
    try {
	responseFromGetRequestOfWordStack(wor_s)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to Host:Port", "Connection refused", "Check", here)}
    
    exiting(here)
}

fun wrapperExecuteHttp4kOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_l '$wor_l'")
    try {
	executeHttp4kOfWordList(wor_l)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5001", "Connection refused", "launch Port :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}

fun wrapperExecuteQuickStartOfWordStack (wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_s '$wor_s'")
    try {
	executeQuickStartOfWordStack(wor_s)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:9000", "Connection refused", "Check", here)}

    exiting(here)
}

fun wrapperExecuteServerOfWordStack (wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if (isTrace(here)) println("$here: input wor_s '$wor_s'")
    try {
	executeServerOfWordStack(wor_s)
    }
    catch (e: java.net.ConnectException){
	fatalErrorPrint ("Connection to 127.0.0.1:5001", "Connection refused", "launch Port :\n\tgo to minichain jsm; . config.sh; ipmsd.sh", here)}
    
    exiting(here)
}


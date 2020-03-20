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

// import org.http4k.format.Moshi.auto

//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertNull
//import org.junit.Test
//import org.assertj.core.api.Assertions.assertThat

/**
 * Author : Emile Achadde 12 mars 2020 at 15:13:49+01:00
 * Revision : Response.status done by Emile Achadde 18 mars 2020 at 19:20:17+01:00
 */

data class Name(val value: String)
data class MyHash(val hash: String)


fun http4kFullTest() {
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

    val timingFilter = Filter {
        next: HttpHandler ->
        {
            request: Request ->
            val start = System.currentTimeMillis()
            val response = next(request)
            val latency = System.currentTimeMillis() - start
            println("$here: Request to ${request.uri} took ${latency}ms")
            response
        }
    }

    val compositeFilter = CachingFilters.Response.NoCache().then(timingFilter)
    val filteredApp: HttpHandler = compositeFilter.then(app)

    filteredApp.asServer(Jetty(9000)).start()

    app.asServer(Jetty(9000)).start()
    
    exiting(here)
}

fun http4kInMemoryResponse() {
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
    println("$here: output of inMemoryResponse")
    println(inMemoryResponse)

// Produces:
//    HTTP/1.1 200 OK
//
//
//    hello Bob

    exiting(here)
}

fun http4kQuickStart() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    // https://www.http4k.org/cookbook/container_integration/
    // https://www.http4k.org/quickstart/
    val app = { request: Request -> Response(OK).body("Hello, ${request.query("name")}!") }
    println ("$here: app $app")

    val jettyServer = app.asServer(Jetty(9000)).start()
    println ("$here: jettyServer started on port 9000")
    
    val request = Request(Method.GET, "http://localhost:9000").query("name", "John Doe")
    println ("$here: request $request")
   
    val client = ApacheClient()
    println ("$here: client $client")
    
    println(client(request))

    jettyServer.stop()

    exiting(here)
}

fun http4kRoutesNestable() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // https://www.http4k.org/cookbook/nestable_routes/
    
    val routesWithFilter =
        PrintRequestAndResponse().then(
            routes(
                "/get/{name}" bind GET to { req: Request -> Response(OK).body(req.path("name")!!) },
                "/post/{name}" bind POST to { _: Request -> Response(OK) }
            )
        )
    println(routesWithFilter(Request(GET, "/get/value")))

    val staticWithFilter = PrintRequestAndResponse().then(static(Classpath("cookbook/nestable_routes")))
    val app = routes(
        "/bob" bind routesWithFilter,
        "/static" bind staticWithFilter,
        "/rita" bind routes(
            "/delete/{name}" bind DELETE to { _: Request -> Response(OK) },
            "/post/{name}" bind POST to { _: Request -> Response(OK) }
        ),
        singlePageApp(Classpath("cookbook/nestable_routes"))
    )

    println(app(Request(GET, "/bob/get/value")))
    println(app(Request(GET, "/static/someStaticFile.txt")))
    println(app(Request(GET, "/someSpaResource")))

    exiting(here)
}

fun http4kRoutesSimple() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    //https://www.http4k.org/cookbook/simple_routing/
    val app = routes(
        "bob" bind GET to { Response(OK).body("you GET bob") },
        "rita" bind POST to { Response(OK).body("you POST rita") },
        "sue" bind DELETE to { Response(OK).body("you DELETE sue") }
    )

    println(app(Request(GET, "/bob")))
    println(app(Request(POST, "/rita")))
    println(app(Request(DELETE, "/sue")))
    
    exiting(here)
}

fun http4kServerAsAFunction() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // https://www.http4k.org/cookbook/server_as_a_function/
    // --args="-http4k server function"
    
    val app = { request: Request -> Response(OK).body("Hello, ${request.query("name")}!") }
    println ("$here: app $app")
    val request = Request(Method.GET, "/").query("name", "John Doe")
    println ("$here: request $request")
   
    val response = app(request)
    println ("$here: response $response")

    exiting(here)
}

fun http4kServerJettyFiltered() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val app = { request: Request -> Response(OK).body("Hello, ${request.query("name")}!") }	
    // this is a Filter - it performs pre/post processing on a request or response
    val timingFilter = Filter {
        next: HttpHandler ->
        {
            request: Request ->
            val start = System.currentTimeMillis()
            val response = next(request)
            val latency = System.currentTimeMillis() - start
            println("$here: Request to ${request.uri} took ${latency}ms")
            response
        }
    }

    // we can "stack" filters to create reusable units, and then apply them to an HttpHandler
    val compositeFilter = CachingFilters.Response.NoCache().then(timingFilter)
    val filteredApp: HttpHandler = compositeFilter.then(app)

    // only 1 LOC to mount an app and start it in a container
    filteredApp.asServer(Jetty(9000)).start()

    exiting(here)
}

fun http4kServerJettyFull() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: --args="-http4k server jetty full"
    
    // http4kInMemoryResponse() ---->
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
     // <----- http4kInMemoryResponse()

    // http4kServerJettyFiltered() ---->
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

    // <---- http4kServerJettyFiltered()

    // http4kClientResponse() ---->
    // HTTP clients are also HttpHandlers!
    val client: HttpHandler = OkHttp()

    val response: Response = client(Request(GET, "http://localhost:9000/greet/Bob"))
    println("$here: response")
    println(response)

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
    // <---- http4kClientResponse()

    exiting(here)
}

fun http4kServerJettyStart(): Http4kServer {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: --args="-http4k server jetty start"
    // Ex.: --args="-port jetty 8888 -http4k server jetty start"
    
    // we can bind HttpHandlers (which are just functions
    //    from  Request -> Response) to paths/methods to create a Route,
    // then combine many Routes together to make another HttpHandler

    val porTyp = PortType.make("jetty")
    val porVal = PortProvider().provideOfPortType(porTyp)
    val porInt = porVal.port
    
    val app: HttpHandler = routes(
        "/ping" bind GET to { _: Request ->
				  Response(OK).body("pong!") },
        "/greet/{name}" bind GET to { req: Request ->
	          val name: String? = req.path("name")
            Response(OK).body("hello ${name ?: "anon!"}")
        }
    ) // routes

    println("$here: jetty server started 'http://localhost:$porInt'")

    val result = app.asServer(Jetty(porInt)).start()
    println("$here: output result '$result'")
    
    exiting(here)
    return result
}

fun http4kServerSunHttpStart(): Http4kServer {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: --args="-http4k server sun start"
    // Ex.: --args="-port sun 8000 -http4k server sun start"
    
    // from https://www.http4k.org/ first example

    val porTyp = PortType.make("sun")
    val porVal = PortProvider().provideOfPortType(porTyp)
    val porInt = porVal.port
    
    val app: HttpHandler = { request: Request ->
				 Response(OK).body(request.body)
                           }

    println("$here: sunHttp server started 'http://localhost:$porInt'")

    val server = app.asServer(SunHttp(porInt)).start()

    println("$here: sunHttp server $server")
    
    exiting(here)
    return server
}


fun menuHttp4kOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -http4k client function
    // Ex.: -http4k client response
    // Ex.: -http4k example
    // Ex.: -http4k get port <port-type> host <host-type> route <route>
    // Ex.: -http4k inmemory
    // Ex.: -http4k ipfs post
    // Ex.: -http4k ipfs get stat /etc/spot.json
    // Ex.: -http4k quickstart
    // Ex.: -http4k routes simple
    // Ex.: -http4k routes nestable
    // Ex.: -http4k server function
    // Ex.: -http4k server jetty filtered
    // Ex.: -http4k server jetty full 
    // Ex.: -http4k server jetty start
    // Ex.: -http4k server sun start

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
	try {
 	    val wor = wor_s.pop()
	    val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
		"cli" -> {menuHttp4kClientOfWordStack(wor_s)}
		"ful" -> {http4kFullTest() }
		"get" -> {menuHttp4kClientUrlGetOfWordStack(wor_s)}
		"hel" -> {printHelpOfString("-http4k ")}
		"inm" -> {http4kInMemoryResponse()}
		"ipf" -> {menuHttp4kIpfsOfWordStack(wor_s)}
		"qui" -> {http4kQuickStart()}
		"for" -> {menuHttp4kFormsOfWordStack(wor_s)}
		"rou" -> {menuHttp4kRoutesOfWordStack(wor_s)}
		"ser" -> {menuHttp4kServerOfWordStack(wor_s)}
		else -> {
		    fatalErrorPrint ("command were client|full|get|help|inmemory|forms|routes|server","'$wor'", "Check input", here)
		} // else
	    } // when (wor_3)
	} // try
	catch (e: java.util.EmptyStackException) {
	    fatalErrorPrint ("command were client|full|get|help|inmemory|forms|routes|server","no command", "Check input", here)
	    }
    exiting(here)
}

fun menuHttp4kRoutesOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "nes" -> {http4kRoutesNestable()} 
	    "sim" -> {http4kRoutesSimple()}
	    else -> {
		fatalErrorPrint ("command were 'nes'table or 'sim'ple","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k routes 'nes'table or 'sim'ple","no arguments", "Complete input", here)
    }
    exiting(here)
}

fun menuHttp4kServerJettyOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -http4k server jetty start
    // Ex.: -http4k server jetty filtered
    // Ex.: -http4k server jetty full	

    var done = false
    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
		"fil" -> {http4kServerJettyFiltered()}
		"ful" -> {http4kServerJettyFull()} 
		"sta" -> {http4kServerJettyStart()}
		"sto" -> {
//		    jettyServer.stop()
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

fun menuHttp4kServerOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    // --args="-http4k server function"
    // --args="-http4k server jetty start"
    // --args="-http4k server sun start"
    // --args="-http4k server filtered jetty"
    
    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "hel" -> {printHelpOfString("-http4k server")}
	    "jet" -> {menuHttp4kServerJettyOfWordStack(wor_s)}
	    "sun" -> {menuHttp4kServerSunHttpOfWordStack(wor_s)}
	    "fun" -> {http4kServerAsAFunction()}
	    else -> {
		fatalErrorPrint ("command were 'fun'ction or 'fil'tered","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k server 'fun'ction|'fil'tered","no arguments", "Complete input", here)
    }
    
    exiting(here)
}

fun menuHttp4kServerSunHttpOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)
    
    // Ex.: -http4k server sun start

    var done = false
    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")
    
    while (!done) {
	try {
	    val wor = wor_s.pop()
	    val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	    if(isLoop(here)) println("$here: while wor '$wor'")
	    
	    when (wor_3) {
		"sta" -> {http4kServerSunHttpStart()}
		else -> {
		    fatalErrorPrint ("$here: command were 'sun start|stop","'-server $wor'", "Check input", here)
		}
	    }// when (wor)
	} // try
	catch (e: java.util.EmptyStackException) {done = true} // catch
    } // while
    exiting(here)
}

fun wrapperExecuteHttp4kOfWordList (wor_l: List<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    val wor_s = wordStackOfWordList(wor_l)
    menuHttp4kOfWordStack(wor_s)
    
    exiting(here)
}

